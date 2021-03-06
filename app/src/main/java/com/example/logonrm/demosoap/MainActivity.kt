package com.example.logonrm.demosoap

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {

    private val url = "http://10.3.2.42:8080/CalculadoraWSService/CalculadoraWS?wsdl"
    private val nameSpace = "http://heiderlopes.com.br/"
    private val methodName = "calcular"
    private val soapAction = nameSpace + methodName
    private val parametro1 = "num1"
    private val parametro2 = "num2"
    private val parametro3 = "op"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btCalcular.setOnClickListener {
        CallWebService().execute(edNumero1.text.toString(),
                                 edNumero2.text.toString(),
                                  spOperacoes.selectedItem.toString()  )

            }
    }

    inner class CallWebService: AsyncTask<String,Void,String>(){
        override fun onPostExecute(s: String) {
            tvResultado.text = s
        }

        override fun doInBackground(vararg params: String?): String {

            var result = ""
            val soapObject = SoapObject(nameSpace, methodName)

            val number1info = PropertyInfo()
            number1info.name = parametro1
            number1info.value = params[0]
            number1info.type = Integer::class.java

            soapObject.addProperty(number1info)

            val number2info = PropertyInfo()
            number2info.name = parametro2
            number2info.value = params[1]
            number2info.type = Integer::class.java

            soapObject.addProperty(number2info)

            val opInfo = PropertyInfo()
            opInfo.name = parametro3
            opInfo.value = params[2]
            opInfo.type = String::class.java

            soapObject.addProperty(opInfo)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.setOutputSoapObject(soapObject)

            val httpTransportSE = HttpTransportSE(url)
            try {
                httpTransportSE.call(soapAction, envelope)
                val soapPrimitive = envelope.response
                result = soapPrimitive.toString()
            } catch (e: Exception) {

                e.printStackTrace()
             }
            return result

        }

    }

}
