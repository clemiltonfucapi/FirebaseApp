package com.clemilton.firebaseappsala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clemilton.firebaseappsala.R;
import com.clemilton.firebaseappsala.model.Request;
import com.clemilton.firebaseappsala.model.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH>  {
    private ArrayList<User> listaContatos;
    private Context context;
    private ClickAdapterUser listener;

    private static final int TYPE_ADD = 0 ;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;
    private static final int TYPE_FRIEND=3;
    private static final int TYPE_REJECT=4;


    public void setListener(ClickAdapterUser listener){
        this.listener = listener;
    }

    public UserAdapter(Context c, ArrayList<User> lista){
        this.listaContatos = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if(viewType== TYPE_RECEIVE){
            v = LayoutInflater.from(context)
                    .inflate(R.layout.user_recycler_2,
                            parent,false );
        }else {
            v = LayoutInflater.from(context)
                    .inflate(R.layout.user_recycler,
                            parent, false);
        }
        if(viewType== TYPE_SEND){
            Button b = v.findViewById(R.id.user_recycler_btn_add);
            b.setText("SOLICITADO");
            b.setEnabled(false);
        }if(viewType==TYPE_FRIEND){
            Button b = v.findViewById(R.id.user_recycler_btn_add);
            b.setText("AMIGOS");
            b.setTextColor(context.getResources().getColor(R.color.blue));
        }if(viewType==TYPE_REJECT){
            Button b = v.findViewById(R.id.user_recycler_btn_add);
            b.setText("REJEITADO");
            b.setTextColor(context.getResources().getColor(R.color.red));
            b.setEnabled(false);
        }

        return new UserVH(v,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position) {

        User u = listaContatos.get(position);
        holder.textEmail.setText(u.getEmail());
        holder.textNome.setText(u.getNome());

        //caso usuario nao foi adicionado
        if(u.getTypeRequest().equals(Request.TYPE_ADD)){
            holder.onClickAdd();
        }
        if(u.getTypeRequest().equals(Request.TYPE_RECEIVE)){
            holder.onClickAceppt();
            holder.onClickReject();
        }


    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    @Override
    public int getItemViewType(int position) {
        User contato = listaContatos.get(position);
        if(contato.getTypeRequest().equals(Request.TYPE_SEND)){
            return TYPE_SEND;
        }
        if(contato.getTypeRequest().equals(Request.TYPE_RECEIVE)){
            return TYPE_RECEIVE;
        }if(contato.getTypeRequest().equals(Request.TYPE_FRIEND)){
            return TYPE_FRIEND;
        }if(contato.getTypeRequest().equals(Request.TYPE_REJECT)){
            return TYPE_REJECT;
        }
        return TYPE_ADD;
    }

    public class UserVH extends RecyclerView.ViewHolder{
        TextView textNome;
        TextView textEmail;
        RoundedImageView imgPhoto;
        Button btnAdicionar;
        Button btnAceitar;
        Button btnRejeitar;


        public void onClickAdd(){
            btnAdicionar.setOnClickListener( v -> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    listener.adicionarContato(position);
                }
            });
        }

        public void onClickAceppt(){
            btnAceitar.setOnClickListener( v-> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    listener.aceitarContato(position);
                }
            });
        }

        public void onClickReject(){
            btnRejeitar.setOnClickListener( v-> {
                if(listener!=null){
                    int position = getAdapterPosition();
                    listener.rejeitarContato(position);
                }
            });
        }

        public UserVH(@NonNull View itemView,int viewType) {
            super(itemView);
            textNome = itemView.findViewById(R.id.user_recycler_nome);
            textEmail = itemView.findViewById(R.id.user_recycler_email);
            imgPhoto = itemView.findViewById(R.id.user_recycler_photo);
            if(viewType== TYPE_ADD || viewType== TYPE_SEND) {
                btnAdicionar = itemView.findViewById(R.id.user_recycler_btn_add);
            }else if(viewType== TYPE_RECEIVE){
                btnAceitar=itemView.findViewById(R.id.user_recycler_aceitar);
                btnRejeitar=itemView.findViewById(R.id.user_recycler_rejeitar);
            }

        }
    }

    public  interface ClickAdapterUser{
        void adicionarContato(int position);

        void aceitarContato(int position);

        void rejeitarContato(int position);

    }


}
