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
import com.example.capstonesejahtera.namasaham.ArtoSaham
import com.example.capstonesejahtera.namasaham.BbniSaham
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
import com.example.capstonesejahtera.namasaham.HrumSaham
import com.example.capstonesejahtera.namasaham.IcbpSaham
import com.example.capstonesejahtera.namasaham.IncoSaham
import com.example.capstonesejahtera.namasaham.IndfSaham
import com.example.capstonesejahtera.namasaham.InkpSaham
import com.example.capstonesejahtera.namasaham.IntpSaham
import com.example.capstonesejahtera.namasaham.IsatSaham
import com.example.capstonesejahtera.namasaham.ItmgSaham
import com.example.capstonesejahtera.namasaham.JpfaSaham
import com.example.capstonesejahtera.namasaham.JsmrSaham
import com.example.capstonesejahtera.namasaham.KlbfSaham
import com.example.capstonesejahtera.namasaham.MapiSaham
import com.example.capstonesejahtera.namasaham.MbmaSaham
import com.example.capstonesejahtera.namasaham.MdkaSaham
import com.example.capstonesejahtera.namasaham.MedcSaham
import com.example.capstonesejahtera.namasaham.MtelSaham
import com.example.capstonesejahtera.namasaham.PgasSaham
import com.example.capstonesejahtera.namasaham.PgeoSaham
import com.example.capstonesejahtera.namasaham.PtbaSaham
import com.example.capstonesejahtera.namasaham.PtmpSaham
import com.example.capstonesejahtera.namasaham.ScmaSaham
import com.example.capstonesejahtera.namasaham.SidoSaham
import com.example.capstonesejahtera.namasaham.SmgrSaham
import com.example.capstonesejahtera.namasaham.SrtgSaham
import com.example.capstonesejahtera.namasaham.TbigSaham
import com.example.capstonesejahtera.namasaham.TinsSaham
import com.example.capstonesejahtera.namasaham.TowrSaham
import com.example.capstonesejahtera.namasaham.TpiaSaham
import com.example.capstonesejahtera.namasaham.UntrSaham
import com.example.capstonesejahtera.namasaham.UnvrSaham

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

        // Navigasi ke AcesSahamActivity saat text_aces diklik
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

        val textArto = findViewById<TextView>(R.id.text_arto)
        textArto.setOnClickListener {
            val intent = Intent(this, ArtoSaham::class.java)
            startActivity(intent)
        }

        val textBbni = findViewById<TextView>(R.id.text_bbni)
        textBbni.setOnClickListener {
            val intent = Intent(this, BbniSaham::class.java)
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

        val textItmg = findViewById<TextView>(R.id.text_itmg)
        textItmg.setOnClickListener {
            val intent = Intent(this, ItmgSaham::class.java)
            startActivity(intent)
        }

        val textJpfa = findViewById<TextView>(R.id.text_jpfa)
        textJpfa.setOnClickListener {
            val intent = Intent(this, JpfaSaham::class.java)
            startActivity(intent)
        }

        val textJsmr = findViewById<TextView>(R.id.text_jsmr)
        textJsmr.setOnClickListener {
            val intent = Intent(this, JsmrSaham::class.java)
            startActivity(intent)
        }

        val textKlbf = findViewById<TextView>(R.id.text_klbf)
        textKlbf.setOnClickListener {
            val intent = Intent(this, KlbfSaham::class.java)
            startActivity(intent)
        }

        val textMapi = findViewById<TextView>(R.id.text_mapi)
        textMapi.setOnClickListener {
            val intent = Intent(this, MapiSaham::class.java)
            startActivity(intent)
        }

        val textMbma = findViewById<TextView>(R.id.text_mbma)
        textMbma.setOnClickListener {
            val intent = Intent(this, MbmaSaham::class.java)
            startActivity(intent)
        }

        val textMdka = findViewById<TextView>(R.id.text_mdka)
        textMdka.setOnClickListener {
            val intent = Intent(this, MdkaSaham::class.java)
            startActivity(intent)
        }

        val textMedc = findViewById<TextView>(R.id.text_medc)
        textMedc.setOnClickListener {
            val intent = Intent(this, MedcSaham::class.java)
            startActivity(intent)
        }

        val textMtel = findViewById<TextView>(R.id.text_mtel)
        textMtel.setOnClickListener {
            val intent = Intent(this, MtelSaham::class.java)
            startActivity(intent)
        }

        val textPgas = findViewById<TextView>(R.id.text_pgas)
        textPgas.setOnClickListener {
            val intent = Intent(this, PgasSaham::class.java)
            startActivity(intent)
        }

        val textPgeo = findViewById<TextView>(R.id.text_pgeo)
        textPgeo.setOnClickListener {
            val intent = Intent(this, PgeoSaham::class.java)
            startActivity(intent)
        }

        val textPtba = findViewById<TextView>(R.id.text_ptba)
        textPtba.setOnClickListener {
            val intent = Intent(this, PtbaSaham::class.java)
            startActivity(intent)
        }

        val textPtmp = findViewById<TextView>(R.id.text_ptmp)
        textPtmp.setOnClickListener {
            val intent = Intent(this, PtmpSaham::class.java)
            startActivity(intent)
        }

        val textScma = findViewById<TextView>(R.id.text_scma)
        textScma.setOnClickListener {
            val intent = Intent(this, ScmaSaham::class.java)
            startActivity(intent)
        }

        val textSido = findViewById<TextView>(R.id.text_sido)
        textSido.setOnClickListener {
            val intent = Intent(this, SidoSaham::class.java)
            startActivity(intent)
        }

        val textSmgr = findViewById<TextView>(R.id.text_smgr)
        textSmgr.setOnClickListener {
            val intent = Intent(this, SmgrSaham::class.java)
            startActivity(intent)
        }

        val textSrtg = findViewById<TextView>(R.id.text_srtg)
        textSrtg.setOnClickListener {
            val intent = Intent(this, SrtgSaham::class.java)
            startActivity(intent)
        }

        val textTbig = findViewById<TextView>(R.id.text_tbig)
        textTbig.setOnClickListener {
            val intent = Intent(this, TbigSaham::class.java)
            startActivity(intent)
        }

        val textTins = findViewById<TextView>(R.id.text_tins)
        textTins.setOnClickListener {
            val intent = Intent(this, TinsSaham::class.java)
            startActivity(intent)
        }

        val textTowr = findViewById<TextView>(R.id.text_towr)
        textTowr.setOnClickListener {
            val intent = Intent(this, TowrSaham::class.java)
            startActivity(intent)
        }

        val textTpia = findViewById<TextView>(R.id.text_tpia)
        textTpia.setOnClickListener {
            val intent = Intent(this, TpiaSaham::class.java)
            startActivity(intent)
        }

        val textUntr = findViewById<TextView>(R.id.text_untr)
        textUntr.setOnClickListener {
            val intent = Intent(this, UntrSaham::class.java)
            startActivity(intent)
        }

        val textUnvr = findViewById<TextView>(R.id.text_unvr)
        textUnvr.setOnClickListener {
            val intent = Intent(this, UnvrSaham::class.java)
            startActivity(intent)
        }


    }
}
