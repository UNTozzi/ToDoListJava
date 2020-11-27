package br.usjt.ads20.listadetarefas;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private EditText txtTarefa;
    private ArrayList<Tarefa> tarefas;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTarefa = findViewById(R.id.tarefa);
        listView = findViewById(R.id.lista);
        tarefas = TarefaDAO.recuperarTodas(this);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(!tarefas.get(i).getId().equals("") || tarefas.get(i).getId() != null){
                TarefaDAO.alterarTarefa(tarefas.get(i).getId());
                tarefas.get(i).setFeita(true);
                setAdapter();
            }
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            TarefaDAO.apagarTarefa(tarefas.get(position).getId());
            tarefas.remove(position);
            setAdapter();
            return false;
        });
    }

    public void setAdapter() {
        String estado;
        ArrayList<String> descricao = new ArrayList<>();
        //ordena o arraylist de tarefas
        Collections.sort(tarefas);
        for (Tarefa tarefa : tarefas) {
            if (tarefa.isFeita()) {
                estado = "Feito";
            } else {
                estado = "A fazer";
            }
            descricao.add(tarefa.getTarefa() + ": " + estado);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, descricao);
        listView.setAdapter(adapter);
    }

    public void inserirTarefa(View view) {
        String descricao = txtTarefa.getText().toString();
        Tarefa tarefa = new Tarefa();
        tarefa.setTarefa(descricao);
        String id = TarefaDAO.inserir(tarefa);
        tarefa.setId(id);
        tarefas.add(tarefa);
        txtTarefa.setText("");
        setAdapter();
    }
}