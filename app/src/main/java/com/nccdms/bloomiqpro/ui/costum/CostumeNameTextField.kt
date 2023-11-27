package com.nccdms.bloomiqpro.ui.costum

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.nccdms.bloomiqpro.R

class CostumeNameTextField:TextInputEditText {
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

    private fun init() {
        hint = resources.getString(R.string.username)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = ResourcesCompat.getDrawable(resources, R.drawable.bg_text_fill, null)
        isSingleLine = true

        val personIcon = ResourcesCompat.getDrawable(resources, R.drawable.icon_person_, null)

        val padding = resources.getDimensionPixelSize(R.dimen.icon_padding)
        personIcon?.setBounds(0, 0, personIcon.intrinsicWidth, personIcon.intrinsicHeight)
        compoundDrawablePadding = padding
        setCompoundDrawablesRelativeWithIntrinsicBounds(personIcon, null, null, null)

        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val input = text.toString()
                error = when {
                    input.isEmpty() -> resources.getString(R.string.username_validation_empty)
                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}