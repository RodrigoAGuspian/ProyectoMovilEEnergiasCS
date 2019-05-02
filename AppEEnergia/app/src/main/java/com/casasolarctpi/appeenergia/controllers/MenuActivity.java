package com.casasolarctpi.appeenergia.controllers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.casasolarctpi.appeenergia.R;
import com.casasolarctpi.appeenergia.fragments.ConsultasFragment;
import com.casasolarctpi.appeenergia.fragments.ContactanosFragment;
import com.casasolarctpi.appeenergia.fragments.IndexFragment;
import com.casasolarctpi.appeenergia.fragments.PerfilFragment;
import com.casasolarctpi.appeenergia.fragments.TiempoRealFragment;
import com.casasolarctpi.appeenergia.models.ChildClass;
import com.casasolarctpi.appeenergia.models.Constants;
import com.casasolarctpi.appeenergia.models.ExpandableListAdapter;
import com.casasolarctpi.appeenergia.models.UserData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnClickListener {

    private ExpandableListView expPagina, expConsultas;
    private ExpandableListAdapter listAdapterExpandable, expaAdaperConsultas;
    private String[] listDataHeader;
    private TextView txtTitle;
    private HashMap<String, ChildClass[]> listDataChild, listaDataConsultas;
    private ChildClass [] listChildrenPaginas;
    private ChildClass[] listChildrenConsultas;
    public ConstraintLayout contentViewMenu, cLHome, cLSensor1, cLSensor2, cLSensor3, cLContactanos, cLPerfil, cLCerrarSesion;
    private  DrawerLayout drawer;
    public static DatabaseReference reference;
    boolean bandera =true;
    private FirebaseAuth mAuth;
    public static UserData userData = new UserData();
    public static String userKey = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        inizialite();
        inizialiteFirebaseApp();
        usuarioIdentificado();

        inputListExpandable();
        createExpandableListView();
        if (bandera){
            getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
            getSupportActionBar().setTitle(getResources().getString(R.string.inicio));
            txtTitle.setText(getResources().getString(R.string.inicio));
            bandera=false;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //Inicialización de vistas.
    private void inizialite() {
        contentViewMenu= findViewById(R.id.contentViewMenu);
        //expListView = findViewById(R.id.expandable_list);
        txtTitle = findViewById(R.id.txtTitle);
        drawer = findViewById(R.id.drawer_layout);
        expPagina = findViewById(R.id.expaPaginas);
        expConsultas = findViewById(R.id.expaConsultas);
        cLHome = findViewById(R.id.cLHome);
        cLSensor1 = findViewById(R.id.cLSensor1);
        cLSensor2 = findViewById(R.id.cLSensor2);
        cLSensor3 = findViewById(R.id.cLSensor3);
        cLContactanos = findViewById(R.id.cLContactanos);
        cLPerfil = findViewById(R.id.cLPerfil);
        cLCerrarSesion = findViewById(R.id.cLCerrarSesion);

        cLHome.setOnClickListener(this);
        cLSensor1.setOnClickListener(this);
        cLSensor2.setOnClickListener(this);
        cLSensor3.setOnClickListener(this);
        cLContactanos.setOnClickListener(this);
        cLPerfil.setOnClickListener(this);
        cLCerrarSesion.setOnClickListener(this);

        expPagina.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent,groupPosition);
                return false;
            }
        });



        expConsultas.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                setListViewHeight(parent,groupPosition);
                return false;
            }
        });

    }

    private void inizialiteFirebaseApp(){
        FirebaseApp.initializeApp(this);
        try {FirebaseDatabase.getInstance().setPersistenceEnabled(false);}catch (Exception e){}
        reference= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


    }

    private void usuarioIdentificado(){
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference usuarios = reference.child("usuarios");
        usuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    UserData tmpUserData =child.getValue(UserData.class);
                    assert tmpUserData != null;
                    assert currentUser != null;
                    if (tmpUserData.getEmail().equals(currentUser.getEmail())){
                        userData= tmpUserData;
                        userKey = child.getKey();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Llamado de listas e ingreso de HashMap para el expandablelistview con navigation drawer
    public void inputListExpandable(){
        listDataHeader = Constants.GROUP_LIST;
        listDataChild = new HashMap<>();
        listChildrenPaginas = new ChildClass[Constants.PAGES_LIST.length];

        for (int i=0;i<listChildrenPaginas.length;i++){
            listChildrenPaginas[i]= new ChildClass(Constants.PAGES_LIST[i],R.drawable.ic_link);
        }


        listChildrenConsultas = new ChildClass[Constants.LIST_QUERY.length];

        for (int i=0;i<listChildrenConsultas.length;i++){
            listChildrenConsultas[i]= new ChildClass(Constants.LIST_QUERY[i],R.drawable.ic_file_search);
        }

        listDataChild.put(listDataHeader[0], listChildrenPaginas);

        listaDataConsultas = new HashMap<>();
        listaDataConsultas.put(getResources().getString(R.string.consultas),listChildrenConsultas);



    }


    //Creación del ExpandableListView y agrego del click
    public void createExpandableListView(){
        listAdapterExpandable = new ExpandableListAdapter(this,new String[]{getResources().getString(R.string.paginas)},listDataChild);

        listAdapterExpandable.setOnChildClickListener(new ExpandableListAdapter.OnChildClickListener() {
            @Override
            public void childClick(int groupId, int childId) {
                Intent intent;
                Uri uri = Uri.parse(Constants.LIST_LINKS_CONOCENOS[childId]);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                closeDrawer();

            }
        });



        expaAdaperConsultas = new ExpandableListAdapter(this,new  String[] {getResources().getString(R.string.consultas)},listaDataConsultas);
        expaAdaperConsultas.setOnChildClickListener(new ExpandableListAdapter.OnChildClickListener() {
            @Override
            public void childClick(int groupId, int childId) {
                ConsultasFragment.modoGraficar=childId;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu, new ConsultasFragment()).commit();
                switch (childId){
                    case 0:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.tarjeta1_tiempo);
                        txtTitle.setText(getResources().getString(R.string.tarjeta1_tiempo));
                        closeDrawer();
                        break;

                    case 1:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.tarjeta2_tiempo);
                        txtTitle.setText(getResources().getString(R.string.tarjeta2_tiempo));
                        closeDrawer();
                        break;

                    case 2:

                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.tarjeta3_tiempo);
                        txtTitle.setText(getResources().getString(R.string.tarjeta3_tiempo));
                        closeDrawer();
                        break;

                    case 3:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.tarjetas_tiempo);
                        txtTitle.setText(getResources().getString(R.string.tarjetas_tiempo));
                        closeDrawer();
                        break;

                    case 4:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.potencias_tiempo);
                        txtTitle.setText(getResources().getString(R.string.potencias_tiempo));
                        closeDrawer();
                        break;
                }
            }
        });

        expConsultas.setAdapter(expaAdaperConsultas);
        expPagina.setAdapter(listAdapterExpandable);

        for (int i=0; i<listAdapterExpandable.getGroupCount(); i++){
            //expListView.expandGroup(i); // esta linea de código es para expandir los menús
        }


    }



    public void closeDrawer(){
        try {
            drawer.closeDrawer(GravityCompat.START);
        }catch (Exception ignored){
            Log.e("Error en drawer",ignored.getMessage());
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cLHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new IndexFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.inicio));
                txtTitle.setText(getResources().getString(R.string.inicio));
                closeDrawer();
                break;

            case R.id.cLSensor1:
                TiempoRealFragment.modoGraficar =0;
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new TiempoRealFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.tarjeta1));
                txtTitle.setText(getResources().getString(R.string.tarjeta1));
                try{
                    //TiempoRealFragment.chartTR.clear();
                    //TiempoRealFragment.clearEntries();
                    //TiempoRealFragment.chartTR.setVisibility(View.INVISIBLE);

                }catch (Exception ignored){

                }
                closeDrawer();
                break;

            case R.id.cLContactanos:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new ContactanosFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.contactanos));
                txtTitle.setText(getResources().getString(R.string.contactanos));
                closeDrawer();
                break;

            case R.id.cLPerfil:
                getSupportFragmentManager().beginTransaction().replace(R.id.contentViewMenu,new PerfilFragment()).commit();
                Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.perfil));
                txtTitle.setText(getResources().getString(R.string.perfil));
                closeDrawer();
                break;

            case R.id.cLCerrarSesion:
                Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;


        }


    }
}
