package com.example.practicalweek10

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnScan =findViewById<Button>(R.id.btnScan)
        val btnGenerate=findViewById<Button>(R.id.btnGenerate)
        val imgQR =findViewById<ImageView>(R.id.imgQR)
//remember var cannot run before function (e.g.  event handler )

        btnGenerate.setOnClickListener(){
            val data = "{'username':'pgs','amount':'100','yes':'yes'}"
            // data can pass in to intent can have many using , to separate keys and : for value

//            {"id":"W001","name":"Ali","programme":"RIT","imgURL":"http://www.demo.onmyfinger.com/images/W001.jpg"}

            val writer = QRCodeWriter()
            try{
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
//                this is the size of QR code 512 pixel * 512 pixel
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width){
                    for(y in 0 until height){
                        bmp.setPixel(x,y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                imgQR.setImageBitmap(bmp)
            }catch (e: WriterException){
                e.printStackTrace()
            }


        }

        btnScan.setOnClickListener(){
            val intent = Intent(this, com.journeyapps.barcodescanner.CaptureActivity::class.java)
//            com.journeyapps is refer to the library file (EXPLIcit intent)

            resultLauncher.launch(intent)

        }


    }
    // this is another function( so put it after  onCreate()
    // this function is for press   Scan QR then return result
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val resultIntent: Intent? = result.data

            val contents = resultIntent?.getStringExtra("SCAN_RESULT")
            val obj =JSONObject(contents)
            // to get only the key e.g. in one string have many key u only want second key that need convert the long string which
            //content many key and value to JSONObject and then we just specify key we want then can get the specific value


            findViewById<TextView>(R.id.tvResult).text =  obj.getString("amount").toString()
        }
    }

}