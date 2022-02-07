/* Copyright © 2022 Mastercard. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 =============================================================================*/

package com.mastercard.webintegration.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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