package com.rr.gps_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DatosAdapter extends RecyclerView.Adapter<DatosAdapter.DatosViewHolder>
{
    private Context mCtx;
    private List<Datos> datosList;


    public DatosAdapter(Context mCtx, List<Datos> productList) {
        this.mCtx = mCtx;
        this.datosList = productList;
    }

    @Override
    public DatosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.recycler_layout, null);
        return new DatosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DatosViewHolder holder, int position) {
        final Datos datos = datosList.get(position);


        holder.textViewTalon.setText(datos.getTalon_Localidad());
        holder.textViewPlacas.setText(datos.getPlacas());
        holder.textViewSellos.setText(String.valueOf(datos.getSello()));
        holder.textViewTrans.setText(String.valueOf(datos.getTransportista()));
        holder.textViewNet.setText(datos.getNet());
        holder.textViewFechaCita.setText(datos.getFecha_Cita());
        holder.textViewConfirmacion.setText(datos.getConfirmacion());
        holder.textViewFechaHora.setText(String.valueOf(datos.getFecha_Cita_Hora()));

        holder.btnSemaforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, SemaforoActivity.class);
                i.putExtra("talon",datos.getTalon_Localidad());
                mCtx.startActivity(i);
            }
        });

        holder.btnTalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, TalonActivity.class);
                mCtx.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return datosList.size();
    }

    class DatosViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTalon, textViewPlacas, textViewSellos, textViewTrans,textViewNet, textViewFechaCita, textViewConfirmacion, textViewFechaHora;
        Button btnSemaforo, btnTalon;

        public DatosViewHolder(View itemView) {
            super(itemView);

            textViewTalon = itemView.findViewById(R.id.txtTalonEdit);
            textViewPlacas = itemView.findViewById(R.id.txtPlacasEdit);
            textViewSellos = itemView.findViewById(R.id.txtSelloEdit);
            textViewTrans = itemView.findViewById(R.id.txtTransportistaEdit);
            textViewNet = itemView.findViewById(R.id.txtNetEdit);
            textViewFechaCita = itemView.findViewById(R.id.txtFechaCitaEdit);
            textViewConfirmacion = itemView.findViewById(R.id.txtConfirmacionEdit);
            textViewFechaHora = itemView.findViewById(R.id.txtHoraEdit);
            btnSemaforo = itemView.findViewById(R.id.btnSemaforoDatos);
            btnTalon = itemView.findViewById(R.id.btnTalonDatos);
        }
    }
}
