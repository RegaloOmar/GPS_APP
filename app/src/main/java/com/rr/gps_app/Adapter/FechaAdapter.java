package com.rr.gps_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rr.gps_app.Class.Fechas;
import com.rr.gps_app.MainActivity;
import com.rr.gps_app.R;

import java.util.List;

public class FechaAdapter extends RecyclerView.Adapter<FechaAdapter.FechasViewHolder>
{
    private Context mCtx;
    private List<Fechas> fechasList;


    public FechaAdapter(Context mCtx, List<Fechas> productList) {
        this.mCtx = mCtx;
        this.fechasList = productList;
    }

    @Override
    public FechaAdapter.FechasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.fechas_list, null);
        return new FechaAdapter.FechasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FechaAdapter.FechasViewHolder holder, int position) {
        final Fechas fechas = fechasList.get(position);


        holder.textViewTalon.setText(fechas.getTalon_Localidad());
        holder.textViewPlacas.setText(fechas.getPlacas());
        holder.textViewSellos.setText(String.valueOf(fechas.getSello()));
        holder.textViewTrans.setText(String.valueOf(fechas.getTransportista()));
        holder.textViewNet.setText(fechas.getNet());
        holder.textViewFechaCita.setText(fechas.getFecha_Cita());
        holder.textViewConfirmacion.setText(String.valueOf(fechas.getConfirmacion()));
        holder.textViewFechaHora.setText(String.valueOf(fechas.getFecha_Cita_Hora()));

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx, MainActivity.class);
                i.putExtra("talon",fechas.getTalon_Localidad());
                mCtx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fechasList.size();
    }

    class FechasViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTalon, textViewPlacas, textViewSellos, textViewTrans,textViewNet, textViewFechaCita, textViewConfirmacion, textViewFechaHora;
        Button btnAccept;

        public FechasViewHolder(View itemView) {
            super(itemView);

            textViewTalon = itemView.findViewById(R.id.textTalon);
            textViewPlacas = itemView.findViewById(R.id.textPlacas);
            textViewSellos = itemView.findViewById(R.id.textSello);
            textViewTrans = itemView.findViewById(R.id.textTransportista);
            textViewNet = itemView.findViewById(R.id.textNet);
            textViewFechaCita = itemView.findViewById(R.id.textFecha);
            textViewConfirmacion = itemView.findViewById(R.id.textConfirm);
            textViewFechaHora = itemView.findViewById(R.id.textHora);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}

