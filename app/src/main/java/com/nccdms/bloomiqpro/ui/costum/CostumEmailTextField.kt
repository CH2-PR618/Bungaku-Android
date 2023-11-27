package com.nccdms.bloomiqpro.ui.costum

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputEditText
import com.nccdms.bloomiqpro.R

class CostumEmailTextField:TextInputEditText {

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
        hint = resources.getString(R.string.email_address)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = ResourcesCompat.getDrawable(resources, R.drawable.bg_text_fill, null)
        isSingleLine = true

        val envelopeIcon = ResourcesCompat.getDrawable(resources, R.drawable.icon_envelope_fill, null)

        val padding = resources.getDimensionPixelSize(R.dimen.icon_padding)
        envelopeIcon?.setBounds(0, 0, envelopeIcon.intrinsicWidth, envelopeIcon.intrinsicHeight)
        compoundDrawablePadding = padding
        setCompoundDrawablesRelativeWithIntrinsicBounds(envelopeIcon, null, null, null)

        addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val input = text.toString()
                error = when {
                    input.isEmpty() -> resources.getString(R.string.email_validation_empty)
                    !Patterns.EMAIL_ADDRESS.matcher(input)
                        .matches() -> resources.getString(R.string.email_validation)

                    else -> null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

}