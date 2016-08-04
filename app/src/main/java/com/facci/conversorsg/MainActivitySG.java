package com.facci.conversorsg;

import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivitySG extends AppCompatActivity {
    final String[] datos = new String[]{"DOLAR", "EURO", "PESO MEXICANO"};

    private Spinner monedaActualSC;
    private Spinner monedaCambioSC;
    private EditText valorCambioSO;
    private TextView resultadoSG;

    final  private double factorDolarEuro = 0.87;
    final private  double factorPesoDolar = 0.54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_sg);

        ArrayAdapter<String> adapadador = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,datos);

        monedaActualSC = (Spinner) findViewById(R.id.monedaActualSC);

        monedaActualSC.setAdapter(adapadador);

        monedaCambioSC = (Spinner) findViewById(R.id.monedaCambioSC);

        SharedPreferences preferencias = getSharedPreferences("MiPreferencia", Context.MODE_PRIVATE);

        String tmpMonedaActual = preferencias.getString("monedaActual", "");
        String tmpMonedaCambio = preferencias.getString("monedaCambio","");

        if(tmpMonedaActual.equals("")){
            int indice = adapadador.getPosition(tmpMonedaActual);
            monedaActualSC.setSelection(indice);
        }

        if(tmpMonedaCambio.equals("")){
            int indice = adapadador.getPosition(tmpMonedaCambio);
            monedaCambioSC.setSelection(indice);
        }
    }

    public void clickConvert(View v){
        monedaActualSC = (Spinner) findViewById(R.id.monedaActualSC);
        monedaCambioSC = (Spinner) findViewById(R.id.monedaCambioSC);
        valorCambioSO = (EditText)findViewById(R.id.valorCambioSO);
        resultadoSG = (TextView) findViewById(R.id.resultadoSG);

        String monedaActual = monedaActualSC.getSelectedItem().toString();
        String monedaCambio = monedaCambioSC.getSelectedItem().toString();

        double valorCambio = Double.parseDouble(valorCambioSO.getText().toString());

        double resultado = procesarConversion(monedaActual, monedaCambio, valorCambio);

        if (resultado>0){
            resultadoSG.setText(String.format("Por %5.2 %s, usted recibira %5.2 %s", valorCambio, monedaActual,resultado,monedaCambio));
            valorCambioSO.setText("");

            SharedPreferences preferencias = getSharedPreferences("MiPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();

            editor.putString("monedaActual", monedaActual);
            editor.putString("monedaCambio", monedaCambio);

            editor.commit();
        }else
        {
            resultadoSG.setText(String.format("Usted Recibira"));
            Toast.makeText(MainActivitySG.this,"Las opciones elegidas no tiene un factor de conversor", Toast.LENGTH_SHORT).show();
        }
    }

    private SharedPreferences getSharedPreferences(String miPreferencias, int modePrivate) {
    }

    private double procesarConversion(String monedaActual, String monedaCambio, double valorCambio){

        double resultadoConversion = 0;

        switch (monedaActual){

            case "DOLAR":
                if(monedaCambio.equals("EURO"))
                    resultadoConversion = valorCambio * factorDolarEuro;

                if(monedaCambio.equals("PESO MEXICANO"))
                    resultadoConversion = valorCambio / factorPesoDolar;
                break;

            case "EURO":
                if(monedaCambio.equals("DOLAR"))
                    resultadoConversion = valorCambio / factorDolarEuro;
                break;

            case "PESO MEXICANO":
                if(monedaCambio.equals("DOLAR"))
                    resultadoConversion = valorCambio * factorDolarEuro;
                break;
        }

        return 0;
    }
}