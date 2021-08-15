package br.utfpr.menucontextoflutuante;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewNomes;
    private EditText editTextNome;
    private ImageButton buttonCancelar, buttonSalvar;
    private List<String> lista;
    private ArrayAdapter<String> adapter;
    private int posicaoAlteracao = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewNomes = findViewById(R.id.listViewNomes);
        editTextNome = findViewById(R.id.editTextNome);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonSalvar = findViewById(R.id.buttonSalvar);

        criarLista();
        buttonCancelar.setVisibility(View.INVISIBLE);

        //Exibir menu ao segurar clicado, nos itens da lista.
        registerForContextMenu(listViewNomes);
    }

    private void criarLista() {
        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewNomes.setAdapter(adapter);
    }


    public void adicionar(View view){

        String frase = editTextNome.getText().toString();

        if(frase.isEmpty()) return;

        editTextNome.setText(null);

        if(posicaoAlteracao == -1){
            lista.add(frase);
        }else{
            lista.set(posicaoAlteracao, frase);
            modoCancelar(view);
        }

        Collections.sort(lista);
        adapter.notifyDataSetChanged();
    }

    private void modoAlterar(int position) {

        String frase = lista.get(position);

        editTextNome.setText(frase);
        editTextNome.setSelection(editTextNome.getText().length());

        buttonSalvar.setImageResource(android.R.drawable.ic_menu_save);
        buttonCancelar.setVisibility(View.VISIBLE);

        //não acessar outras opções da tela.
        listViewNomes.setEnabled(false);

        posicaoAlteracao = position;

    }

    private void excluir(int position) {
        lista.remove(position);
        adapter.notifyDataSetChanged();
    }

    public void modoCancelar(View view){

        //Limpa o campo nome
        editTextNome.setText(null);
        //Troca imagem do botão salvar
        buttonSalvar.setImageResource(android.R.drawable.ic_input_add);
        //Esconde botão cancelar
        buttonCancelar.setVisibility(View.INVISIBLE);
        //Destrava opçóes da tela.
        listViewNomes.setEnabled(true);
        //posicao padrão.
        posicaoAlteracao = -1;
    }

    //inflar o menu na tela principal
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.main_menu_contexto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //informacoes do menu de contexto
        AdapterView.AdapterContextMenuInfo info;
        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.menuItemAlterar:
                modoAlterar(info.position);
                return true;
            case R.id.menuItemExcluir:
                excluir(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}