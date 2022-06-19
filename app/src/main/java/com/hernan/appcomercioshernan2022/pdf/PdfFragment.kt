package com.hernan.appcomercioshernan2022.pdf

import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hernan.appcomercioshernan2022.databinding.FragmentPdfBinding
import com.hernan.appcomercioshernan2022.enlace_con_firebase.viewmodels_crud.ViewModelFirestore
import com.hernan.appcomercioshernan2022.modelos_de_datos.ModeloDeIndumentaria
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class PdfFragment : Fragment() {
    private lateinit var binding:FragmentPdfBinding
    private val viewModelFirestore:ViewModelFirestore by activityViewModels()
    var layoutManager:RecyclerView.LayoutManager? = null
    var adapter:AdapterPdf? = null
    private val STORAGE_CODE: Int = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPdfBinding.inflate(inflater, container, false)

        binding.btGuardarPdf.setOnClickListener {

            //necesitamos manejar permisos de tiempo de ejecución para dispositivos con marshmallow  y superior
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                //sistema operativo> = marshMallow (6.0), verifique que el permiso esté habilitado o no
                if (checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    // no se otorgó permiso, solicítelo
                    val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }
                else{
                    //permiso ya otorgado, llamar al método savepdf

                    savePdf()
                }
            }
            else{
                //permiso ya otorgado, llamar al método savepdf
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    savePdf()
                }
            }

        }


        sumatoriaPrecios()
        inflarRecycler()
        return binding.root
    }

    fun sumatoriaPrecios(){

        val resultado = viewModelFirestore.sumaArray.sum()
        binding.textResultado.text = "suma total $ $resultado"
    }
    private fun inflarRecycler(){
        val data = viewModelFirestore.dataListaCarrito

        layoutManager = LinearLayoutManager(context)
        binding.recyclerListaCompra.layoutManager = layoutManager
        adapter = AdapterPdf(data as ArrayList<ModeloDeIndumentaria>)
        binding.recyclerListaCompra.adapter = adapter




    }
    private fun savePdf() {

        val arrayDatos = viewModelFirestore.dataListaCarrito

        val mDoc = com.itextpdf.text.Document()
        // pdf. nombre del archivo
        Log.e("Mdoc pdf", mDoc.toString())

        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".Pdf"
        Log.e("Filepath en PDF", mFilePath.toString())

        try {
            Log.e("DATA en Adapter PDF", arrayDatos.toString())

            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            Log.e("DATA en Adapter PDF 2", arrayDatos.toString())

            mDoc.open()

            val mPrecioTotal = binding.textResultado.text.toString()

            mDoc.addAuthor("Hernan Torres")

            val table = PdfPTable(4)

            table.headerRows = 1
            table.addCell(PdfPCell(Phrase("Producto", Font(Font.FontFamily.HELVETICA, 16f,Font.BOLD))))
            table.addCell(PdfPCell(Phrase("Cantidad", Font(Font.FontFamily.HELVETICA, 16f,Font.BOLD))))
            table.addCell(PdfPCell(Phrase("Precio", Font(Font.FontFamily.HELVETICA, 16f,Font.BOLD))))
            table.addCell(PdfPCell(Phrase("Subtotal", Font(Font.FontFamily.HELVETICA, 16f,Font.BOLD))))


            for (list in arrayDatos){
                Log.e("List en PDF", list.nombre.toString())

                table.addCell(PdfPCell(Phrase(list.nombre, Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.cantidad.toString(), Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.precio.toString(), Font(Font.FontFamily.HELVETICA, 12f))))
                table.addCell(PdfPCell(Phrase(list.subtotal.toString(), Font(Font.FontFamily.HELVETICA, 12f))))
            }
            //   val arrayLista = arrayListOf("$mPro                                  $mCan                                   $mPro                        $mTot")
            //   mDoc.add(Paragraph(arrayLista.toString()))
            /* for (list in arrayLista){
                 val lista = "$mPro \t $mCan \t $mPRe \t $mTot"
                 mDoc.add(Paragraph(lista))
             }*/



            //  for (dat in  arrayDatos!!){
            //    val text = "${dat.producto}                                          \t ${dat.cantidad}                             \t ${dat.precio}                        \t ${dat.precioTotal}"
            //  mDoc.add(Paragraph(text))
            //}

            mDoc.add(table)
            val preT =Paragraph(mPrecioTotal, Font(Font.FontFamily.HELVETICA, 15f, Font.BOLD))
            preT.alignment = Element.ALIGN_RIGHT
            mDoc.add(preT)

            mDoc.close()
            Toast.makeText(context, " $mFileName.pdf\nse guardó en \n$mFilePath", Toast.LENGTH_SHORT).show()

        }
        catch (e: Exception){}

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // se otorgó el permiso de la ventana emergente, llama a savePdf()
                    savePdf()
                }
                else {
                    // se denegó el permiso de la ventana emergente, muestra mensaje de error
                    Toast.makeText(context, "permiso denegado", Toast.LENGTH_SHORT).show()


                }
            }
        }
        // esto en el activity principal
    }


}