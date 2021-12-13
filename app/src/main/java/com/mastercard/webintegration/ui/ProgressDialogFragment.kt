package com.mastercard.webintegration.ui

import android.app.Dialog
import android.graphics.Color
import com.google.android.material.textfield.TextInputLayout
import android.graphics.ColorFilter
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Bundle
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * A simple [Fragment] subclass.
 * Helps to show progress indicator on screen.
 * It can be used as :
 * <pre>
 * `//To show progress indicator
 * ProgressDialogFragment.newInstance().showProgressDialog(fragmentManager);
 *
 * //To dismiss progress indicator
 * ProgressDialogFragment.newInstance().dismissProgressDialog();
` *
</pre> *
 */
class ProgressDialogFragment : DialogFragment() {
    var progressDialog: Dialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        progressDialog = Dialog(context!!)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressDialog!!.setContentView(progressBar)
        isCancelable = false
        return progressDialog!!
    }

    companion object {
        private var progressDialogFragment: ProgressDialogFragment? = null

        /**
         * Shows progress indicator
         */
        fun showProgressDialog(fragmentManager: FragmentManager?) {
            if (instance != null && !instance!!
                    .isAdded
            ) {
                instance!!.show(
                    fragmentManager!!, null
                )
            }
        }

        private val instance: ProgressDialogFragment?
            private get() {
                if (progressDialogFragment == null) {
                    progressDialogFragment = ProgressDialogFragment()
                }
                return progressDialogFragment
            }

        /**
         * Dismisses progress indicator
         */
        fun dismissProgressDialog() {
            if (progressDialogFragment != null) {
                progressDialogFragment!!.dismissAllowingStateLoss()
            }
        }
    }
}