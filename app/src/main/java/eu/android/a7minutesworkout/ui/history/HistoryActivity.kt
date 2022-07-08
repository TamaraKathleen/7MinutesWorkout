package eu.android.a7minutesworkout.ui.history

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import eu.android.a7minutesworkout.R
import eu.android.a7minutesworkout.WorkOutApp
import eu.android.a7minutesworkout.database.HistoryDao
import eu.android.a7minutesworkout.database.HistoryDatabase
import eu.android.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    var isAdapretOn = false

    private val dao by lazy {
        HistoryDatabase.getInstance(this).historyDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistoryActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "HISTORY"

        binding.toolbarHistoryActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (application as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteAllDataList()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllDataList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you want to delete the history?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                dao.deleteAllDates()
                Toast.makeText(
                    applicationContext,
                    "History deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                binding.tvHistory.visibility = GONE
                binding.rvHistory.visibility = GONE
                binding.tvNoDataAvailable.visibility = VISIBLE
                dialogInterface.dismiss()
            }

        }

        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


}

    private fun getAllCompletedDates(historyDao: HistoryDao) {

        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDatesList ->
                if (allCompletedDatesList.isNotEmpty()) {
                    binding.tvHistory.visibility = VISIBLE
                    binding.rvHistory.visibility = VISIBLE
                    binding.tvNoDataAvailable.visibility = GONE

                    binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)

                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList) {
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(ArrayList(dates))

                    binding.rvHistory.adapter = historyAdapter
                } else {
                    binding.tvHistory.visibility = GONE
                    binding.rvHistory.visibility = GONE
                    binding.tvNoDataAvailable.visibility = VISIBLE
                }

            }
        }
    }


}