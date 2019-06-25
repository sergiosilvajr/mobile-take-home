package com.sergiosilvajr.guestlogicchallenge.cardinfo

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sergiosilvajr.guestlogicchallenge.R
import com.sergiosilvajr.guestlogicchallenge.R.string
import com.sergiosilvajr.guestlogicchallenge.data.FileManager
import kotlinx.android.synthetic.main.fragment_cardinfo.*
import kotlinx.android.synthetic.main.fragment_cardinfo.view.*


class CardInfoFragment : Fragment(), CardInfoContracts.View {
    var contract: CardInfoContract? = null
    var presenter: CardInfoContracts.Presenter? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            contract = context as CardInfoContract
        } catch (error: ClassCastException) {
            print("You must implement CardInfoContract to use CardInfoFragment")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cardinfo, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CardInfoPresenter(this, FileManager(activity!!.assets))
        initTextAdapters()

        view.search_button.setOnClickListener {
            val originCode = origin_text.text
            val destinyCode = destiny_text.text

            this.presenter?.checkIfCodeIsOk(originCode.toString(), destinyCode.toString())
        }

        view.close_button.setOnClickListener {
            contract?.onClosedPressed()
        }

    }

    private fun initTextAdapters() {
        val airportCodes = presenter?.getListOfValidAirportCodes()

        origin_text.setAdapter(ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, airportCodes))
        origin_text.inputType = InputType.TYPE_CLASS_TEXT
        origin_text.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        origin_text.threshold = 1

        destiny_text.setAdapter(
            ArrayAdapter<String>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                airportCodes
            )
        )
        destiny_text.inputType = InputType.TYPE_CLASS_TEXT
        destiny_text.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        destiny_text.threshold = 1
    }

    override fun showInvalidIATACodeError() {
        val alertDialog = AlertDialog.Builder(activity!!).create()
        alertDialog.setTitle(getString(string.error_title))
        alertDialog.setMessage(getString(string.error_codes))
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(string.ok),
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()
    }

    override fun handleCodes(originCode: String, destinyCode: String) {
        contract?.onStartPressed(originCode, destinyCode)
    }


    companion object {
        fun newInstance(): CardInfoFragment {
            return CardInfoFragment()
        }
    }
}

