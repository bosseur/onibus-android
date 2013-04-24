package br.com.caelum.ondeestaobusao.eventos;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import br.com.caelum.ondeestaobusao.model.Ponto;

public class EventoPontosEncontrados extends BroadcastReceiver{

	private static final String FALHOU = "falhou";
	private static final String MENSAGEM_FALHA = "mensagemFalha";
	private static final String PONTOS = "pontos";
	private static final String PONTOS_ENCONTRADOS = "pontos-encontrados";
	private static final String PONTOS_NAO_ENCONTRADOS = "pontos-nao-encontrados";
	
	private static PontosEncontradosDelegate delegate;

	@Override
	@SuppressWarnings("unchecked")
	public void onReceive(Context context, Intent intent) {
		if (intent.getBooleanExtra(FALHOU, false)) {
			delegate.lidaComFalha((String)intent.getSerializableExtra(MENSAGEM_FALHA));
		} else {
			ArrayList<Ponto> pontos = (ArrayList<Ponto>) intent.getSerializableExtra(PONTOS);			
			delegate.lidaCom(pontos);
		}
		
	}
	public static void notifica(Context context, ArrayList<Ponto> pontos) {
		Intent intent = new Intent(PONTOS_ENCONTRADOS);

		intent.putExtra(PONTOS, pontos);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public static EventoPontosEncontrados registraObservador(PontosEncontradosContextDelegate delegate){
		return registraObservador(delegate, delegate.getContext());
	}
	
	public static EventoPontosEncontrados registraObservador(PontosEncontradosDelegate delegate, Context context){
		EventoPontosEncontrados receiver = new EventoPontosEncontrados();
		EventoPontosEncontrados.delegate = delegate;
		
		LocalBroadcastManager.getInstance(context)
			.registerReceiver(receiver, new IntentFilter(PONTOS_ENCONTRADOS));
		
		LocalBroadcastManager.getInstance(context)
			.registerReceiver(receiver, new IntentFilter(PONTOS_NAO_ENCONTRADOS));
		
		return receiver;
	}
	public static void notificaFalha(Context context, String mensagem) {
		Intent intent = new Intent(PONTOS_NAO_ENCONTRADOS);
		intent.putExtra(MENSAGEM_FALHA, mensagem);
		intent.putExtra(FALHOU, true);
		
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public void unregister(Context context) {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
	}
	
}