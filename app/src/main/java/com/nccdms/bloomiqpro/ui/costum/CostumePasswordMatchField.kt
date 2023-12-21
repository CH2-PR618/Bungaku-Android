package com.nccdms.bloomiqpro.ui.costum

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.nccdms.bloomiqpro.R

class CostumePasswordMatchField: TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        hint = resources.getString(R.string.confirmPassword)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = ResourcesCompat.getDrawable(resources, R.drawable.bg_text_fill, null)
        maxLines = 1

        val passwordIcon = ResourcesCompat.getDrawable(resources, R.drawable.icon_password, null)

        val padding = resources.getDimensionPixelSize(R.dimen.icon_padding)
        passwordIcon?.setBounds(0, 0, passwordIcon.intrinsicWidth, passwordIcon.intrinsicHeight)
        compoundDrawablePadding = padding
        setCompoundDrawablesRelativeWithIntrinsicBounds(passwordIcon, null, null, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = text.toString()
                error = when{
                    input.isEmpty() -> resources.getString(R.string.password_valid_empty)
                    input.length < 8 -> resources.getString(R.string.password_length)
                    else -> null
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

}