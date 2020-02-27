package com.automatoDev.gcdelivery.firebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.automatoDev.gcdelivery.R;
import com.automatoDev.gcdelivery.activity.CarActivity;
import com.automatoDev.gcdelivery.activity.LoginActivity;
import com.automatoDev.gcdelivery.activity.MainActivity;
import com.automatoDev.gcdelivery.provider.CarProvider;
import com.automatoDev.gcdelivery.provider.UserProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class FirebaseOper extends Thread {
    private Activity context;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fireStore;
    private FirebaseStorage fireStorage;
    private CarProvider carProviderGet;
    private UserProvider userProvider;

    private Button btn_goCar_sheet;
    private Button btn_exit_sheet;
    private TextView txt_qtdOrder_sheet;
    private TextView txt_totalOrder_sheet;

    double totalOrder = 0;
    int qtdOrders = 0;
    //Classe de apoio com algumas operações utilizadas no firebase
    //O intuito é diminuir o codigo nas classes principais e a reutilização do codigo no app que servira de painel para o apliacativo.]

    //Construtor da classe no qual receberar um contexto para as operações da classe
    public FirebaseOper(Activity context) {
        //Passagem de parametetro do contexto e inicializações de objetos Firebase utilizados na classe;
        this.context = context;
        firebaseAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        fireStorage = FirebaseStorage.getInstance();
        carProviderGet = new CarProvider();
    }

    @Override
    public void run() {

    }

    //Metodo usado para efetuar o login recebendo como parametro duas strings: email e senha
    public void fireLogin(final String userEmail, final String userPassword) {
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Se a tarefa for bem sucedida retorna um log e chama o metodo para a proxima activity
                            Log.i("logx", "TaskLogin: " + task.getResult());
                            nextActivityMain();
                        }
                    }
                }).addOnFailureListener(context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Em caso de falha exibe um dialog com detalhes ao ususario
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Verifique se o email e senha estão corretos\nDa uma olhadinha na sua internet tambem!");
                alert.setTitle("Opa!");
                alert.setPositiveButton("Ok", null);
                alert.show();
            }
        });
    }
    //####################

    //Metodo para acesso a proxima activity (principal)
    public void nextActivityMain() {
        //recebe um usuario atual se ele existir claro
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && !MainActivity.status) { // sse esse usuario for != null e se a activity ainda nao tiver sido criada executa os codigos
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            context.finish();
        }
    }

    //#################
    //Metodo para logout da aplicação
    public void fireLogout() {
        //Chama o metodo signOut() que faz logout na conta atual, finaliza a activity atual e chama a activity de login
        firebaseAuth.signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    //#######################
    //Metodo para setar / atualizar a foto do usuario recebendo como parametro o uid e a  uri da foto
    public void fireUpdatePic(final String uid, Uri uri) {
        //Cria e ativa uma progressbar para impedir o usuario de manusear o app durante a operação
        View view = context.getLayoutInflater().inflate(R.layout.layout_add_car, null);
        TextView txt_label = view.findViewById(R.id.txt_label);
        txt_label.setText("Subindo arquivo...");

        final AlertDialog alerta = new AlertDialog.Builder(context).create();
        alerta.setCancelable(false);
        alerta.setView(view);
        alerta.show();
        //inicia o processo de upload da imagem no FireStorage
        //A referencia recebe o caminho onde sera guardada a imagem e o nome da imagem neste caso sera o uid do usuario firebaseAuth
        final StorageReference storageReference = fireStorage.getReference("/usersApp/profile/images/" + uid);
        storageReference.putFile(uri)//O metodo putFile(uri) recebe a  uri do parametro do metodo e grava no fireStorage
                .addOnCompleteListener(context, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) { //Ao completar sua tarefa e se ela tiver sucesso, obtem a url da imagem para gravar no FireStore
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(context, new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //Neste evento obtem se a url que sera passado como parametro no update no caminho "users/uid/userUrlPhoto"
                                            fireStore.collection("users").document(uid)
                                                    .update("userUrlPhoto", uri.toString())
                                                    .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //ao completar a tarefa e se tiver sucesso elimina a progressbar leberando a aplicação ao usuario.
                                                            if (task.isSuccessful()) {
                                                                alerta.dismiss();
                                                            }
                                                        }
                                                    });

                                        }
                                    });
                        }
                    }
                    //Em caso de falha  exibe um log para o desenvolvedor
                    // TODO IMPLEENTAR DETALHES PARA O USUSARIO
                }).addOnFailureListener(context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("logx", "ExceptionCreateAcount: " + e.getMessage(), e);
            }
        });
    }

    //#######################
    //Metodo usado para criar um novo usuasrio recebendo parametros UserProvider completo, string email e password
    public void fireCreateUser(final UserProvider userProvider, String userEmail, String userPassword) {
        //Cria e ativa uma progressbar para impedir o usuario de manusear o app durante a operação
        View view = context.getLayoutInflater().inflate(R.layout.layout_add_car, null);
        TextView txt_label = view.findViewById(R.id.txt_label);
        txt_label.setText("Criando usuario...");

        final AlertDialog alerta = new AlertDialog.Builder(context).create();
        alerta.setCancelable(false);
        alerta.setView(view);
        alerta.show();
        //inicia o processo de craição do ususario, ao completar sua tarefa o usuario ja estara logado na intancia do app
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //ao completar a tarefa e se tiver sucesso
                        // inicia o processo de upload dos dados no banco
                        if (task.isSuccessful()) {
                            userProvider.setUserUid(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid());
                            fireStore.collection("users").document(userProvider.getUserUid()).set(userProvider)
                                    .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //ao completar a tarefa e se tiver sucesso
                                            // elimina a progressbar, chama o metodo para acesso a nova activity e finaliza a activity
                                            if (task.isSuccessful()) {
                                                alerta.dismiss();
                                                nextActivityMain();
                                                context.finish();
                                            }
                                        }
                                        //Em caso de facha exibe um log para averiguação do desenvolvedor
                                        //TODO IMPLEMETNAR DETALHES DO ERRO PARA O USUARIO
                                    }).addOnFailureListener(context, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("logx", "ExceptioSaveNewUser: " + e.getMessage(), e);
                                }
                            });
                        }
                    }
                    //Em caso de facha exibe uma dialog com detalhes para o usuario
                }).addOnFailureListener(context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Verifique se o email esta correto\nDa uma olhadinha na sua internet tambem!");
                alert.setTitle("Opa!");
                alert.setPositiveButton("Ok", null);
                alert.show();
            }
        });
    }

    //############################
    //Metodo usado para salvar o pedido com os itens do carrinho do usuasrio
    public void fireSaveOrders(final CarProvider carProvider, final String uid) {
        //Cria e ativa uma progressbar para impedir o usuario de manusear o app durante a operação
        View view = context.getLayoutInflater().inflate(R.layout.layout_add_car, null);
        TextView txt_label = view.findViewById(R.id.txt_label);
        txt_label.setText("Adicionando ao carrinho...");
        final AlertDialog progress = new AlertDialog.Builder(context).create();
        progress.setCancelable(false);
        progress.setView(view);
        progress.show();

        //Usado para formatação de moeda loacl
        final Locale locale = new Locale("pt", "BR"); //Atribui ao locale o idioma e o pais
        final NumberFormat nb = NumberFormat.getCurrencyInstance(locale); // cria uma instancia do objeto NumbFormat passando como parametro o locale

        final View details = context.getLayoutInflater().inflate(R.layout.layout_car, null);

        btn_exit_sheet = details.findViewById(R.id.btn_exit_sheet);
        btn_goCar_sheet = details.findViewById(R.id.btn_goCar_sheet);
        txt_qtdOrder_sheet = details.findViewById(R.id.txt_qtdOrder_sheet);
        txt_totalOrder_sheet = details.findViewById(R.id.txt_totalOrder_sheet);

        final BottomSheetDialog sheet = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        fireStore.collection("users").document(uid).collection("orders")
                .document(UUID.randomUUID().toString())
                .set(carProvider)
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        fireStore.collection("users").document(uid)
                                .collection("orders")
                                .get().addOnCompleteListener(context, new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot doc : task.getResult()) {

                                        carProviderGet = doc.toObject(CarProvider.class);
                                        totalOrder += carProviderGet.getTotalOrder();
                                        qtdOrders++;

                                    }

                                }

                                txt_qtdOrder_sheet.setText(String.valueOf(qtdOrders));
                                txt_totalOrder_sheet.setText(nb.format(totalOrder));
                                btn_exit_sheet.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        qtdOrders = 0;
                                        totalOrder = 0;
                                        context.finish();
                                    }
                                });
                                btn_goCar_sheet.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, CarActivity.class);
                                        context.startActivity(intent);
                                        context.finish();
                                    }
                                });
                                sheet.setCancelable(false);
                                sheet.setContentView(details);
                                progress.dismiss();
                                sheet.show();

                            }
                        });
                    }
                });
    }
    //###############################

    public boolean getUser(String uid) {
            fireStore.collection("users").document(uid)
                    .get().addOnCompleteListener(context, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists())
                        userProvider = doc.toObject(UserProvider.class);
                }
            });
            if (userProvider == null)
                return false;
            else
                return true;
    }

    //##################################
    //Metodo para limpar o carrinho
    public void delOrders(String uid) {
        View view = context.getLayoutInflater().inflate(R.layout.layout_add_car, null);
        TextView txt_label = view.findViewById(R.id.txt_label);
        txt_label.setText("Limpando carrinho..");
        final AlertDialog alerta = new AlertDialog.Builder(context).create();
        alerta.setCancelable(false);
        alerta.setView(view);
        alerta.show();


        fireStore.collection("users").document(uid)
                .collection("orders")
                .get()
                .addOnCompleteListener(context, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            if (doc.exists())
                                doc.getReference().delete();
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        alerta.dismiss();
                        Toast.makeText(context, "Feito!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fireUpdateUser(UserProvider userProvider,String uid){
        View view = context.getLayoutInflater().inflate(R.layout.layout_add_car,null);
        TextView txt_label = view.findViewById(R.id.txt_label);
        txt_label.setText("Atualizando perfil \n Aguarde...");

        final AlertDialog save = new AlertDialog.Builder(context).create();
        save.setView(view);
        save.show();

        fireStore.collection("users").document(uid).update(userProvider.toMap())
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Perfil atualizdo \n com sucesso!!", Toast.LENGTH_LONG).show();
                        save.dismiss();
                        context.finish();
                    }
                }).addOnFailureListener(context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //TODO IMPLEMENTAR METODO DE RETORNO AO USUARIO EM CASO DE FALHA
            }
        });
    }
}
