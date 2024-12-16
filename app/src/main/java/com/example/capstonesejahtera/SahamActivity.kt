package com.example.capstonesejahtera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capstonesejahtera.namasaham.AcesSaham
import com.example.capstonesejahtera.namasaham.AdroSaham
import com.example.capstonesejahtera.namasaham.AkraSaham
import com.example.capstonesejahtera.namasaham.AmmnSaham
import com.example.capstonesejahtera.namasaham.AmrtSaham
import com.example.capstonesejahtera.namasaham.AntmSaham
import com.example.capstonesejahtera.namasaham.ArtoSaham
import com.example.capstonesejahtera.namasaham.AsiiSaham
import com.example.capstonesejahtera.namasaham.BbcaSaham
import com.example.capstonesejahtera.namasaham.BbniSaham
import com.example.capstonesejahtera.namasaham.BbriSaham
import com.example.capstonesejahtera.namasaham.BbtnSaham
import com.example.capstonesejahtera.namasaham.BmriSaham
import com.example.capstonesejahtera.namasaham.BrisSaham
import com.example.capstonesejahtera.namasaham.BrptSaham
import com.example.capstonesejahtera.namasaham.BukaSaham
import com.example.capstonesejahtera.namasaham.CpinSaham
import com.example.capstonesejahtera.namasaham.EmtkSaham
import com.example.capstonesejahtera.namasaham.EssaSaham
import com.example.capstonesejahtera.namasaham.ExclSaham
import com.example.capstonesejahtera.namasaham.GgrmSaham
import com.example.capstonesejahtera.namasaham.GotoSaham
import com.example.capstonesejahtera.namasaham.HrumSaham
import com.example.capstonesejahtera.namasaham.IcbpSaham
import com.example.capstonesejahtera.namasaham.IncoSaham
import com.example.capstonesejahtera.namasaham.IndfSaham
import com.example.capstonesejahtera.namasaham.IndySaham
import com.example.capstonesejahtera.namasaham.InkpSaham
import com.example.capstonesejahtera.namasaham.IntpSaham
import com.example.capstonesejahtera.namasaham.IsatSaham

class SahamActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_saham)

        // Menyesuaikan padding dengan insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sahamhal)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val textAces = findViewById<TextView>(R.id.text_aces)
        textAces.setOnClickListener {
            val intent = Intent(this, AcesSaham::class.java)
            startActivity(intent)
        }

        val textAdro = findViewById<TextView>(R.id.text_adro)
        textAdro.setOnClickListener {
            val intent = Intent(this, AdroSaham::class.java)
            startActivity(intent)
        }


        val textAkra = findViewById<TextView>(R.id.text_akra)
        textAkra.setOnClickListener {
            val intent = Intent(this, AkraSaham::class.java)
            startActivity(intent)
        }

        val textAmmn = findViewById<TextView>(R.id.text_ammn)
        textAmmn.setOnClickListener {
            val intent = Intent(this, AmmnSaham::class.java)
            startActivity(intent)
        }

        val textAmrt = findViewById<TextView>(R.id.text_amrt)
        textAmrt.setOnClickListener {
            val intent = Intent(this, AmrtSaham::class.java)
            startActivity(intent)
        }

        val textAntm = findViewById<TextView>(R.id.text_antm)
        textAntm.setOnClickListener {
            val intent = Intent(this, AntmSaham::class.java)
            startActivity(intent)
        }

        val textArto = findViewById<TextView>(R.id.text_arto)
        textArto.setOnClickListener {
            val intent = Intent(this, ArtoSaham::class.java)
            startActivity(intent)
        }

        val textTins = findViewById<TextView>(R.id.text_asii)
        textTins.setOnClickListener {
            val intent = Intent(this, AsiiSaham::class.java)
            startActivity(intent)
        }

        val textUntr = findViewById<TextView>(R.id.text_bbca)
        textUntr.setOnClickListener {
            val intent = Intent(this, BbcaSaham::class.java)
            startActivity(intent)
        }

        val textBbni = findViewById<TextView>(R.id.text_bbni)
        textBbni.setOnClickListener {
            val intent = Intent(this, BbniSaham::class.java)
            startActivity(intent)
        }

        val textTpia = findViewById<TextView>(R.id.text_bbri)
        textTpia.setOnClickListener {
            val intent = Intent(this, BbriSaham::class.java)
            startActivity(intent)
        }

        val textBbtn = findViewById<TextView>(R.id.text_bbtn)
        textBbtn.setOnClickListener {
            val intent = Intent(this, BbtnSaham::class.java)
            startActivity(intent)
        }

        val textBmri = findViewById<TextView>(R.id.text_bmri)
        textBmri.setOnClickListener {
            val intent = Intent(this, BmriSaham::class.java)
            startActivity(intent)
        }

        val textBris = findViewById<TextView>(R.id.text_bris)
        textBris.setOnClickListener {
            val intent = Intent(this, BrisSaham::class.java)
            startActivity(intent)
        }

        val textBrpt = findViewById<TextView>(R.id.text_brpt)
        textBrpt.setOnClickListener {
            val intent = Intent(this, BrptSaham::class.java)
            startActivity(intent)
        }

        val textBuka = findViewById<TextView>(R.id.text_buka)
        textBuka.setOnClickListener {
            val intent = Intent(this, BukaSaham::class.java)
            startActivity(intent)
        }

        val textCpin = findViewById<TextView>(R.id.text_cpin)
        textCpin.setOnClickListener {
            val intent = Intent(this, CpinSaham::class.java)
            startActivity(intent)
        }

        val textEmtk = findViewById<TextView>(R.id.text_emtk)
        textEmtk.setOnClickListener {
            val intent = Intent(this, EmtkSaham::class.java)
            startActivity(intent)
        }

        val textEssa = findViewById<TextView>(R.id.text_essa)
        textEssa.setOnClickListener {
            val intent = Intent(this, EssaSaham::class.java)
            startActivity(intent)
        }

        val textExcl = findViewById<TextView>(R.id.text_excl)
        textExcl.setOnClickListener {
            val intent = Intent(this, ExclSaham::class.java)
            startActivity(intent)
        }

        val textGgrm = findViewById<TextView>(R.id.text_ggrm)
        textGgrm.setOnClickListener {
            val intent = Intent(this, GgrmSaham::class.java)
            startActivity(intent)
        }

        val textHrum = findViewById<TextView>(R.id.text_hrum)
        textHrum.setOnClickListener {
            val intent = Intent(this, HrumSaham::class.java)
            startActivity(intent)
        }

        val textIcbp = findViewById<TextView>(R.id.text_icbp)
        textIcbp.setOnClickListener {
            val intent = Intent(this, IcbpSaham::class.java)
            startActivity(intent)
        }

        val textInco = findViewById<TextView>(R.id.text_inco)
        textInco.setOnClickListener {
            val intent = Intent(this, IncoSaham::class.java)
            startActivity(intent)
        }

        val textIndf = findViewById<TextView>(R.id.text_indf)
        textIndf.setOnClickListener {
            val intent = Intent(this, IndfSaham::class.java)
            startActivity(intent)
        }

        val textTowr = findViewById<TextView>(R.id.text_indy)
        textTowr.setOnClickListener {
            val intent = Intent(this, IndySaham::class.java)
            startActivity(intent)
        }

        val textInkp = findViewById<TextView>(R.id.text_inkp)
        textInkp.setOnClickListener {
            val intent = Intent(this, InkpSaham::class.java)
            startActivity(intent)
        }

        val textIntp = findViewById<TextView>(R.id.text_intp)
        textIntp.setOnClickListener {
            val intent = Intent(this, IntpSaham::class.java)
            startActivity(intent)
        }

        val textIsat = findViewById<TextView>(R.id.text_isat)
        textIsat.setOnClickListener {
            val intent = Intent(this, IsatSaham::class.java)
            startActivity(intent)
        }

        val textTbig = findViewById<TextView>(R.id.text_goto)
        textTbig.setOnClickListener {
            val intent = Intent(this, GotoSaham::class.java)
            startActivity(intent)
        }

    }
}
