package br.usjt.ads20.listadetarefas;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TarefaDAO {
    // Access a Cloud Firestore instance from your Activity
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static String inserir(Tarefa tarefa){
        Map<String, Object> tarefaMap = new HashMap<>();
        tarefaMap.put("tarefa", tarefa.getTarefa());
        tarefaMap.put("feita", tarefa.isFeita());

        db.collection("tarefas")
                .add(tarefaMap)
                .addOnSuccessListener(documentReference -> tarefa.setId(documentReference.getId()))
                .addOnFailureListener(e -> { });
        return tarefa.getId();
    }

    public static ArrayList<Tarefa> recuperarTodas(MainActivity activity){
        ArrayList<Tarefa> tarefas = new ArrayList<>();
        db.collection("tarefas")
                .get()
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        for(QueryDocumentSnapshot document: task.getResult()){
                            Log.d(Tarefa.TAG, document.getId() + " => " + document.getData());
                            Tarefa tarefa = new Tarefa();
                            tarefa.setId(document.getId());
                            tarefa.setTarefa(document.get("tarefa").toString());
                            tarefa.setFeita(Boolean.parseBoolean(document.get("feita").toString()));
                            tarefas.add(tarefa);
                        }
                        activity.setAdapter();
                    } else {
                        Log.w(Tarefa.TAG, "Erro na recuperação dos documentos.", task.getException());
                    }
                });
        return tarefas;
    }

    public static void apagarTarefa(String id){
        db.collection("tarefas").document(id)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(Tarefa.TAG, "Documento apagado: " + id))
                .addOnFailureListener(e -> Log.w(Tarefa.TAG, "Erro ao apagar documento.", e));
    }

    public static void alterarTarefa(String id) {
        db.collection("tarefas").document(id).update("feita", true)
                .addOnSuccessListener(aVoid -> Log.d(Tarefa.TAG, "Documento alterado: " + id))
                .addOnFailureListener(e -> Log.w(Tarefa.TAG, "Erro ao alterar tarefa.", e));
    }
}
