package com.clemilton.firebaseappsala.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clemilton.firebaseappsala.R;
import com.clemilton.firebaseappsala.adapter.UserAdapter;
import com.clemilton.firebaseappsala.model.Request;
import com.clemilton.firebaseappsala.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private RecyclerView recyclerContatos;
    private UserAdapter userAdapter;
    private DatabaseReference usersRef = FirebaseDatabase.getInstance()
                                            .getReference("users");
    private DatabaseReference requestRef = FirebaseDatabase.getInstance()
                                            .getReference("requests");

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User userLogged;

    private ArrayList<User> listaContatos = new ArrayList<>();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_main, container, false);
        userLogged = new User(
            auth.getCurrentUser().getUid(),
            auth.getCurrentUser().getEmail(),
            auth.getCurrentUser().getDisplayName()
        );

        recyclerContatos = layout.findViewById(R.id.frag_main_recycler_user);

        userAdapter = new UserAdapter(getContext(),listaContatos);
        userAdapter.setListener(new UserAdapter.ClickAdapterUser() {
            @Override
            public void adicionarContato(int position) {
                User u = listaContatos.get(position);

                Request reqSend = new Request(u.getId(),Request.TYPE_SEND);
                Request reqReceive = new Request(userLogged.getId(),Request.TYPE_RECEIVE);

                // request send
                requestRef.child(userLogged.getId())
                        .child(reqSend.getUserId())
                        .setValue(reqSend);

                // request receive
                requestRef.child(u.getId())
                        .child(reqReceive.getUserId())
                        .setValue(reqReceive);

                //tirar o usuario solicitado
                listaContatos.get(position).setTypeRequest(Request.TYPE_SEND);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void aceitarContato(int position) {
                Toast.makeText(getContext(),"Aceitar: "+listaContatos.get(position).getNome(),Toast.LENGTH_SHORT ).show();
                User user = listaContatos.get(position);

                Request reqLog = new Request(user.getId(),Request.TYPE_FRIEND);
                Request reqUser = new Request(userLogged.getId(),Request.TYPE_FRIEND);

                //req para usuario logado
                requestRef.child(userLogged.getId())
                        .child(reqLog.getUserId())
                        .setValue(reqLog);

                //req para outro usuario
                requestRef.child(user.getId())
                        .child(reqUser.getUserId())
                        .setValue(reqUser);

            }

            @Override
            public void rejeitarContato(int position) {
                Toast.makeText(getContext(),"Rejeitar: "+listaContatos.get(position).getNome(),Toast.LENGTH_SHORT ).show();
                User user = listaContatos.get(position);
                //criando solicitacao rejeitada
                Request req = new Request(user.getId(),Request.TYPE_REJECT);

                //alterar somente para usuario logado
                requestRef.child(userLogged.getId())
                        .child(req.getUserId())
                        .setValue(req);
            }
        });
        recyclerContatos.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerContatos.setAdapter(userAdapter);



        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsersDatabase();
    }

    public void getUsersDatabase(){
        //usuarios que ja foram solicitados
        Map<String, Request> mapRequest  = new HashMap<String,Request>();
        requestRef.child(userLogged.getId())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot r : snapshot.getChildren()){
                    Request request = r.getValue(Request.class);
                    mapRequest.put(request.getUserId(),request);
                }

                //ler no usuarios
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaContatos.clear();
                        for(DataSnapshot u : snapshot.getChildren()) {
                            User user = u.getValue(User.class);
                            //selecionando o tipo do request
                            Request req = mapRequest.get(user.getId());
                            if(req==null){
                                user.setTypeRequest(Request.TYPE_ADD);
                            }else {
                                user.setTypeRequest(req.getType());
                            }

                            if(!userLogged.equals(user)){
                                listaContatos.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        requestRef.child(userLogged.getId()).child("send")
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot u : snapshot.getChildren()){
                    User user = u.getValue(User.class);
                    // adicionando usuario no HashMap
                    mapUsersReq.put(user.getId(),user);
                }
                //ler o no usuarios
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaContatos.clear();
                        for(DataSnapshot u : snapshot.getChildren()){
                            User user = u.getValue(User.class);
                            if(mapUsersReq.containsKey(user.getId())){
                                user.setRequest(User.USUARIO_SOLICITADO);
                            }
                            if(!userLogged.equals(user)){
                                listaContatos.add(user);
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }
}
