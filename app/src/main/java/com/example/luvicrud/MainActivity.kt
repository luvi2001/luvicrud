package com.example.luvicrud

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.SimpleCursorAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextUpdate:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper=DatabaseHelper(this)
        editTextAge=findViewById(R.id.editTextAge)
        editTextName=findViewById(R.id.editTextName)
        editTextUpdate=findViewById(R.id.editTextId)

        val btnInsert: Button=findViewById(R.id.btnInsert)
        btnInsert.setOnClickListener{insertData()}

        val btnUpdate: Button=findViewById(R.id.btnUpdate)
        btnUpdate.setOnClickListener{updateData()}

        val btnRead: Button=findViewById(R.id.btnRead)
        btnRead.setOnClickListener{displayData()}

        val btnDelete: Button=findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener{deleteData()}
    }

    private fun insertData(){
        val name=editTextName.text.toString()
        val age=editTextAge.text.toString()

        if(name.isNotBlank() && age.isNotBlank()){
            val id=dbHelper.insertData(name,age.toFloat())

            if(id>0){
                editTextName.text.clear()
                editTextAge.text.clear()
                hideKeyboard()
                displayData()
            }
        }

        editTextUpdate.visibility= View.GONE
    }

    private fun displayData() {
        val cursor=dbHelper.readData()
        val columns= arrayOf(DatabaseHelper.COL_ID,DatabaseHelper.COL_NAME,DatabaseHelper.COL_AGE)
        val toViews= intArrayOf(R.id.textID,R.id.textName,R.id.textAge)
        val adapter=SimpleCursorAdapter(
            this,R.layout.list_item,cursor,columns,toViews,0
        )

        val listView=findViewById<ListView>(R.id.listView)
        listView.adapter=adapter
    }

    private fun hideKeyboard() {
        val view =this.currentFocus
        if(view!=null){
            val key=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            key.hideSoftInputFromWindow(view.windowToken,0)
        }
    }

    private fun updateData(){
        editTextUpdate.visibility=View.VISIBLE
        val name=editTextName.text.toString()
        val age= editTextAge.text.toString()
        val id = editTextUpdate.text.toString()

        if(name.isNotBlank() && age.isNotBlank()){
            val updateRows= dbHelper.updateData(id,name,age.toFloat())
            if(updateRows>0){
                editTextName.text.clear()
                editTextName.text.clear()
                editTextUpdate.text.clear()

                hideKeyboard()
                displayData()
            }
        }


      }

    private fun deleteData(){
      editTextUpdate.visibility=View.VISIBLE
        val id=editTextUpdate.text.toString()
        val idToDelete=id
        val deleteRow=dbHelper.deleteData(idToDelete)
        if(deleteRow>0){
            editTextName.text.clear()
            editTextAge.text.clear()
            editTextUpdate.text.clear()

            hideKeyboard()
            displayData()
        }
    }
}