package com.example.sqllitedemo

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_detail.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.dialog_update.tvCancel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListofDataIntoRecyclerView()
        btn_add.setOnClickListener {view  ->
            addRecord()
            setupListofDataIntoRecyclerView()
        }
    }
    private fun addRecord(){
        val name = namaINP.text.toString()
        val email = emailINP.text.toString()
        val alamat= alamatINP.text.toString()
        val databaseHandler : DatabaseHandler = DatabaseHandler(this)
        if (!name.isEmpty() && !email.isEmpty()) {
            val status = databaseHandler.addEmployee(EmpModelClass(0,name, email,alamat))
            if (status > -1) {
                Toast.makeText(applicationContext,"Record Saved",Toast.LENGTH_SHORT).show()
                namaINP.text.clear()
                emailINP.text.clear()
                alamatINP.text.clear()
                closeKeyBoard()
            }
        }else{
            Toast.makeText(applicationContext, "name or email cannot",Toast.LENGTH_SHORT).show()
        }
    }

    private fun closeKeyBoard(){
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken,0)

        }
    }


    private fun getItemList() : ArrayList<EmpModelClass> {
        val databaseHandler : DatabaseHandler = DatabaseHandler(this)
        val empList : ArrayList<EmpModelClass> = databaseHandler.viewEmployee()

        return empList
    }

    private fun setupListofDataIntoRecyclerView(){
        if (getItemList().size > 0){
            data_RecycleView.visibility = View.VISIBLE
            tvRecordsAvailable.visibility = View.GONE

            data_RecycleView.layoutManager = LinearLayoutManager(this)
            val itemAdapter = itemAdapter (this, getItemList())
            data_RecycleView.adapter = itemAdapter
        }else{
            data_RecycleView.visibility = View.GONE
            tvRecordsAvailable.visibility = View.VISIBLE
        }
    }

    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")

        builder.setMessage("Are you sure")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("yes") { dialogInterface, which ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", "",""))

            if (status > -1) {
                Toast.makeText(applicationContext, "Record delete succesfully", Toast.LENGTH_SHORT).show()
                setupListofDataIntoRecyclerView()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, which: Int ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun updateRecordDialog (empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)

        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModelClass.name)
        updateDialog.etUpdateEmailId.setText(empModelClass.email)
        updateDialog.etUpdateAlamat.setText(empModelClass.alamat)

        updateDialog.tvUpdate.setOnClickListener(View.OnClickListener{
           val name = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmailId.text.toString()
            val alamat = updateDialog.etUpdateAlamat.text.toString()

            val databaseHandler : DatabaseHandler = DatabaseHandler(this)

            if (!name.isEmpty() && !email.isEmpty()){
                val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id,name, email,alamat))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Recor Update", Toast.LENGTH_SHORT).show()
                    setupListofDataIntoRecyclerView()
                    updateDialog.dismiss()
                    closeKeyBoard()
                }
            }else{
                Toast.makeText(applicationContext, "Name or Email cannot blank",Toast.LENGTH_SHORT).show()
            }
        })
        updateDialog.tvCancel.setOnClickListener(View.OnClickListener{
        updateDialog.dismiss()
        })
        updateDialog.show()
        closeKeyBoard()
    }

    fun detailRecordDialog (empModelClass: EmpModelClass){
        val detailDialog = Dialog(this, R.style.Theme_Dialog)

        detailDialog.setCancelable(true)
        detailDialog.setContentView(R.layout.dialog_detail)

        detailDialog.tvName.setText(empModelClass.name)
        detailDialog.tvEmail.setText(empModelClass.email)
        detailDialog.tvAlamat.setText(empModelClass.alamat)

        detailDialog.tvClose.setOnClickListener(View.OnClickListener{
            detailDialog.dismiss()
        })
        detailDialog.show()
    }
}