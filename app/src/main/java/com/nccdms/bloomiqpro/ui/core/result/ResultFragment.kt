package com.nccdms.bloomiqpro.ui.core.result

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nccdms.bloomiqpro.R
import com.nccdms.bloomiqpro.databinding.DialogSelectImageBinding
import com.nccdms.bloomiqpro.databinding.FragmentResultBinding
import com.nccdms.bloomiqpro.ml.Bungaku
import com.nccdms.bloomiqpro.ui.core.camera.CameraActivity
import com.nccdms.bloomiqpro.utils.BaseFragment
import com.nccdms.bloomiqpro.utils.CameraUtils.CAMERA_BUNDLE_KEY
import com.nccdms.bloomiqpro.utils.CameraUtils.CAMERA_REQUEST_KEY
import com.nccdms.bloomiqpro.utils.Permission.CAMERA
import dagger.hilt.android.AndroidEntryPoint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

@AndroidEntryPoint
class ResultFragment : BaseFragment() {
    private var _binding : FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: ResultViewModel by viewModels()
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewmodel.setUri(it)
        } ?: run {
            showToast("No image selected")
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermitted ->
        if (isPermitted)
            navigateToCamera()
        else
            showToast("Please allow the camera permission to use this feature")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserve()
        setupEventClick()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setupObserve() {
        viewmodel.uri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                showImage(true)
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireContext().contentResolver,
                            it
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                }
                binding.ivInputFlower.setImageBitmap(bitmap)
                outputGenerator(bitmap)
            }
        }
    }

    private fun outputGenerator(bitmap: Bitmap) {
        // Load the TensorFlow Lite model
        val model = Bungaku.newInstance(requireContext())

        // Ensure the input bitmap has a compatible configuration
        val compatibleBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Preprocess the input image (assuming the model expects a 224x224 RGB image)
        val resizedBitmap = Bitmap.createScaledBitmap(compatibleBitmap, 224, 224, true)
        val inputBuffer = ByteBuffer.allocateDirect(1 * 224 * 224 * 3 * 4) // 4 bytes per float
        inputBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.width, 0, 0, resizedBitmap.width, resizedBitmap.height)
        var pixel = 0
        for (i in 0 until 224) {
            for (j in 0 until 224) {
                val value = intValues[pixel++]
                inputBuffer.putFloat((value shr 16 and 0xFF) / 255.0f)
                inputBuffer.putFloat((value shr 8 and 0xFF) / 255.0f)
                inputBuffer.putFloat((value and 0xFF) / 255.0f)
            }
        }

        // Create a TensorBuffer with the input data
        val inputTensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputTensorBuffer.loadBuffer(inputBuffer)

        // Run model inference and get the result
        val outputs = model.process(inputTensorBuffer)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Convert the output TensorBuffer to a FloatArray
        val outputValues = outputFeature0.floatArray

        // Print the raw output values for inspection
        for (i in outputValues.indices) {
            Log.d("ModelOutput", "Output $i: ${outputValues[i]}")
        }
        // Map the numeric output to a flower name and percentage
        val (flowerName, percentage) = mapOutputToFlower(outputValues)

        // Update the TextViews in your binding
        binding.tvResult.text = flowerName
        binding.tvPercentageValue.text = "$percentage%"

        //Set click for result
        binding.tvResult.setOnClickListener {
            if (flowerName.lowercase() != "unknown" && binding.tvResult.text.isNotEmpty()){
                val action =
                    ResultFragmentDirections.actionResultFragmentToDetailFragment(flowerName.lowercase())
                findNavController().navigate(action)
            }else{
                showToast("Flower not found")
            }
        }

        // Release model resources
        model.close()
    }

    private fun mapOutputToFlower(outputValues: FloatArray): Pair<String, Float>  {
        // Find the index with the highest probability
        val predictedIndex = outputValues.indices.maxByOrNull { outputValues[it] } ?: 0
        // Map the index to a flower name
        val flowerName = when (predictedIndex) {
            0 -> ""
            1 -> "Iris lavigata"
            2 -> "Black-eyed Susan"
            3 -> "Calendula officinalis"
            4 -> "California poppy"
            5 -> "Carnation"
            6 -> "Marguerite"
            7 -> "Coreopsis Lanceolata"
            8 -> "Dandelion"
            9 -> "Plumeria"
            10 -> "Roses"
            11 -> "Sunflower"
            12 -> "Tulip"
            13 -> "Water Lily"
            else -> "Unknown"
        }
        // Find the confidence (percentage) corresponding to the predicted class
        val percentage = (outputValues[predictedIndex] * 100)

        // Set as "Unknown" if the confidence is below 0.7
        return if (percentage > 70) {
            Pair(flowerName, percentage)
        } else {
            Pair("Unknown", percentage)
        }
    }


    private fun setupEventClick() {
        binding.apply {
            btnLoadCamera.setOnClickListener{
                navigateToGallery()
            }
            btnTakePicture.setOnClickListener{
                if (permissionGranted(CAMERA)) {
                navigateToCamera()
                } else {
                    cameraPermissionLauncher.launch(CAMERA.value)
                }
            }
            ivInputFlower.setOnClickListener{
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogSelectImageBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.maxWidth = ViewGroup.LayoutParams.MATCH_PARENT
        dialogBinding.btnDelete.setOnClickListener {
            showImage(false)
            viewmodel.clearUri()
            binding.tvResult.text = getString(R.string.result_here)
            binding.tvPercentageValue.text = ""
            dialog.dismiss()
        }
        dialog.show()
    }

    @Suppress("DEPRECATION")
    private fun navigateToCamera() {
        val cameraIntent = Intent(requireContext(), CameraActivity::class.java)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_KEY)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_KEY && resultCode == Activity.RESULT_OK) {
            val uriString = data?.getStringExtra(CAMERA_BUNDLE_KEY)
            val uri = uriString?.toUri()

            uri?.let {
                // Handle the received URI in the Fragment
                viewmodel.setUri(it)
            } ?: run {
                showToast("No image captured")
            }
        }
    }

    private fun navigateToGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(hasImage: Boolean) {
        binding.ivInputFlower.visibility = if (hasImage) View.VISIBLE else View.GONE
        binding.ivFlower.root.visibility = if (hasImage) View.GONE else View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}