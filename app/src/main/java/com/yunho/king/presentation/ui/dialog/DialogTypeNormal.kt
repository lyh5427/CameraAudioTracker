package com.yunho.king.presentation.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yunho.king.Const
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.Utils.toVisible
import com.yunho.king.databinding.FragmentDialogTypeNormalBinding
import com.yunho.king.domain.listener.DialogNormalType1Listener
import com.yunho.king.domain.listener.DialogNormalType2Listener

/**
 * Title
 * Content
 * Btn ( 1개 or 2개 )
 *
 * TYPE1 = btn 1개  / btnText = okText
 * TYPE2 = btn 2개
 */
class DialogTypeNormal : DialogFragment() {

    private lateinit var binding: FragmentDialogTypeNormalBinding
    private lateinit var title: String
    private lateinit var content: String
    private lateinit var type: String
    private lateinit var okText: String
    private lateinit var cancelText: String
    private lateinit var type1Listener: DialogNormalType1Listener
    private lateinit var type2Listener: DialogNormalType2Listener

    private var cancelable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(Const.TITLE)!!
            content = it.getString(Const.CONTENT)!!
            type = it.getString(Const.TYPE)!!
            okText = it.getString(Const.OK_TEXT)!!
            cancelText = it.getString(Const.CANCEL_TEXT)!!
            cancelable = it.getBoolean(Const.CANCELABLE)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogTypeNormalBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setView()
        setClickListener()
        setWindow()

    }

    private fun setWindow() {
        val param = dialog?.window?.attributes
        param!!.width = if (resources.displayMetrics.widthPixels > 1500) {
            1000
        } else {
            (resources.displayMetrics.widthPixels * 0.8).toInt()
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        param.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = param
        dialog?.setCancelable(cancelable)
    }

    private fun setView() = with(binding) {
        title.text = this@DialogTypeNormal.title
        content.text = this@DialogTypeNormal.content

        when (this@DialogTypeNormal.type) {
            Const.DIALOG_TYPE1 -> {
                type1Ok.text = this@DialogTypeNormal.okText
                type1Ok.toVisible()
            }

            else -> {
                type2Ok.text = this@DialogTypeNormal.okText
                type2Cancel.text = this@DialogTypeNormal.cancelText
                layoutType2.toVisible()
            }
        }
    }

    private fun setClickListener() = with(binding) {
        type2Ok.singleClickListener {
            dialog!!.dismiss()
            type2Listener.clickOk()
        }

        type2Cancel.singleClickListener {
            dialog!!.dismiss()
            type2Listener.clickCancel()
        }

        type1Ok.singleClickListener {
            dialog!!.dismiss()
            type1Listener.onClick()
        }
    }

    fun regType1Listener(listener: DialogNormalType1Listener) {
        type1Listener = listener
    }

    fun regType2Listener(listener: DialogNormalType2Listener) {
        type2Listener = listener
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String,
                        content: String,
                        type: String,
                        okText: String,
                        cancelText: String,
                        cancelable: Boolean = false) =
            DialogTypeNormal().apply {
                arguments = Bundle().apply {
                    putString(Const.TITLE, title)
                    putString(Const.CONTENT, content)
                    putString(Const.TYPE, type)
                    putBoolean(Const.CANCELABLE, cancelable)
                    putString(Const.OK_TEXT, okText) // Type1 일때 ok text 사용
                    putString(Const.CANCEL_TEXT, cancelText)
                }
            }
    }
}

