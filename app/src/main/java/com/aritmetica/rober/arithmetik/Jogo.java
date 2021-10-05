package com.aritmetica.rober.arithmetik;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.aritmetica.roliveira.arithmetik.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Jogo extends Activity {
	public static final String NOME_FICHEIRO="ListaErradas";
	private static int numCertas, numErradas, myScore;
	private Boolean treinar, som=true;
	private Tabuada tabuada;
	private int nivel, numArray;
	private String rpCerta;
	private long duracao;
	private List<Pergunta> lstErradas= new ArrayList<>();
	private final Handler handler=new Handler();
	private TextView txtCertas, txtErradas, txtTime, txtScore, lblScore,txtpergunta;
	private ImageView imgtimeglass;
	private final Button[] btn=new Button[4];
	private SharedPreferences prefs;
	private MediaPlayer mpCerto, mpErrado;
	private MyCount cron;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jogo);

		Typeface font3= Typeface.createFromAsset(getAssets(), "fontastique.ttf");

		// Obtem referencias de controlos gráficos
		txtpergunta = findViewById(R.id.txtPergunta);
		imgtimeglass= findViewById(R.id.imgTime);
		txtCertas= findViewById(R.id.txtRight);
		txtErradas= findViewById(R.id.txtWrong);
		txtTime = findViewById(R.id.txtTime);
		txtScore= findViewById(R.id.txtScore);
		lblScore= findViewById(R.id.lblScore);


		btn[0] = findViewById(R.id.btnR0);
		btn[1] = findViewById(R.id.btnR1);
		btn[2] = findViewById(R.id.btnR2);
		btn[3] = findViewById(R.id.btnR3);

		txtpergunta.setTypeface(font3);

		btn[0].setTypeface(font3);
		btn[1].setTypeface(font3);
		btn[2].setTypeface(font3);
		btn[3].setTypeface(font3);


		btn[0].setOnTouchListener(new ButtonTouchListener(0));
		btn[1].setOnTouchListener(new ButtonTouchListener(1));
		btn[2].setOnTouchListener(new ButtonTouchListener(2));
		btn[3].setOnTouchListener(new ButtonTouchListener(3));

		// Obtém os dados do ficheiro de preferencias (configurações)
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		som = prefs.getBoolean("som", true);
		nivel = Integer.parseInt(prefs.getString("nivel_dificuldade", "10"));
		myScore=prefs.getInt("score", 0);
		duracao = Long.parseLong(prefs.getString("tempo", "20"))*1000;
		cron=new MyCount(duracao+1000);

		Intent intent=getIntent();
		treinar=intent.getExtras().getBoolean("treinar");

		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	protected void onResume() {
    	super.onResume();
    	mpCerto = MediaPlayer.create(getApplicationContext(), R.raw.certa);
    	mpErrado = MediaPlayer.create(this, R.raw.errada);
    	numCertas=0;
		numErradas=0;
		txtCertas.setText("0");
		txtErradas.setText("0");
		inicializarJogo();
    }

	protected void onPause() {
		super.onPause();
			if (!cron.haveStoped()){
				SharedPreferences.Editor editor = prefs.edit();
				editor.putLong("tempo_atual", cron.atualTime);
				editor.apply();
				cron.cancel();
		}

		//escreveFicheiro();
		QuestionIO.writeToFile(getBaseContext(),NOME_FICHEIRO,lstErradas);
		mpCerto.release();
	    mpErrado.release();
	}

	public void finish(){
		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("score", myScore);
		edit.apply();
		super.finish();
	}

	private void inicializarJogo() {
		//lstErradas=leFicheiro();
		Toast.makeText(getBaseContext(),"vou ler",Toast.LENGTH_SHORT).show();
		lstErradas=QuestionIO.readFromFile(getBaseContext(),NOME_FICHEIRO);
		Toast.makeText(getBaseContext(),"já li",Toast.LENGTH_SHORT).show();
		if (treinar){
			numArray=lstErradas.size()-1;
			txtTime.setVisibility(View.INVISIBLE);
			imgtimeglass.setVisibility(View.INVISIBLE);
			lblScore.setVisibility(View.INVISIBLE);
			txtScore.setVisibility(View.INVISIBLE);
			novajogada();
		} else {
			txtTime.setVisibility(View.VISIBLE);
			imgtimeglass.setVisibility(View.VISIBLE);

			String tipoTabuada=prefs.getString("tipo_tabuada", "x");
			switch (tipoTabuada){
				case "n":
					tabuada=new TabuadaANaturais();
					break;
				case "x":
					tabuada=new TabuadaMult();
				break;
				case "+":
					tabuada=new TabuadaAdicao();
				break;
				case "-":
					tabuada=new TabuadaSub();
				break;
				case "*":
					tabuada=new TabuadaExpNum();
					break;
				case "m":
					tabuada=new TabuadaMultRel();
					break;
				case "ma":
					tabuada=new TabuadaExpNumMultAd();
					break;
				case ":":
					tabuada=new TabuadaDivisao();
					break;
				default:
					tabuada=new TabuadaMult();
			}

			tabuada.setMaximo(this.nivel);
			atualizaLayout();

			Thread c = new Thread(cron);
			c.start();
		}
		numCertas=0;
		numErradas=0;
		txtCertas.setText("0");
		txtErradas.setText("0");

	}

	private void atualizaLayout() {
		txtpergunta.setText(tabuada.PtoString());
		txtScore.setText(Integer.toString(myScore));

		// Check if we're running on Android 5.0 or higher
        // Call some material design APIs here
        btn[0].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
        btn[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
        btn[2].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
        btn[3].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));

        btn[0].setText(tabuada.getResposta(0));
		btn[1].setText(tabuada.getResposta(1));
		btn[2].setText(tabuada.getResposta(2));
		btn[3].setText(tabuada.getResposta(3));
		rpCerta=tabuada.getCerta();

		//Toast.makeText(getBaseContext(),txtpergunta.getText(),Toast.LENGTH_SHORT).show();

	}
		// atualiza layout de treino
	private void atualizaLayout(Pergunta per){

		txtpergunta.setText(per.getTxtper());

			btn[0].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
			btn[1].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
			btn[2].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));
			btn[3].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColor));


		btn[0].setText(per.getRespostas(0));
		btn[1].setText(per.getRespostas(1));
		btn[2].setText(per.getRespostas(2));
		btn[3].setText(per.getRespostas(3));
		rpCerta=per.getRespCerta();
	}

	private void novajogada(){

		if (treinar){
			try {
				Pergunta p=lstErradas.get(numArray);
				atualizaLayout(p);
			} catch (Exception e){
				inicializarJogo();
			}

        } else {
			tabuada.geraDesafio();
			atualizaLayout();
		}
		btn[0].setEnabled(true);
		btn[1].setEnabled(true);
		btn[2].setEnabled(true);
		btn[3].setEnabled(true);
	}

	class MyCount extends CountDownTimer implements Runnable {
		private long atualTime;
		public MyCount(long millisInFuture) {
			super(millisInFuture, 1000);
		}
		@Override
		public void run() {
			this.start();
		}
		@Override
		public void onFinish() {
			txtTime.setText("0");
			acabouTempo();
		}
		@Override
		public void onTick(long millisUntilFinished) {
			this.atualTime=millisUntilFinished;
			txtTime.setText(String.valueOf(this.atualTime / 1000));
		}

		public boolean haveStoped(){
			return this.atualTime <= 0;
		}
	}

	private void acabouTempo(){
		AlertDialog.Builder builder = new AlertDialog.Builder(
				Jogo.this);
		builder.setMessage(R.string.FimTempoJogar)
		.setCancelable(false)
		.setPositiveButton(R.string.Sim,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int id) {
						duracao = Long.parseLong(prefs.getString("tempo", "20"))*1000;
						inicializarJogo();
					}
				})
		.setNegativeButton(R.string.Nao,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
										int id) {
						Jogo.this.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		/*TextView textView = alert.findViewById(android.R.id.message);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.AlertDialogTextSizePergunta));
		alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.AlertDialogTextSizeButtons));
		alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.AlertDialogTextSizeButtons));*/
	}

// deprecated replaced by QuestionIO class
	/*private void escreveFicheiro(){
		BufferedWriter writer=null;
		File file = getBaseContext().getFileStreamPath(NOME_FICHEIRO);
		if (lstErradas.size()!=0){
		try{
			writer=new BufferedWriter(new OutputStreamWriter(openFileOutput(NOME_FICHEIRO,MODE_PRIVATE)));
			for(Pergunta p: lstErradas) {
				writer.write(p.toString());
			}
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			if (writer != null){
				try {
					writer.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		} else {
			try {
				file.delete();
			} catch (Exception e) {

			}
		}
	}*/
// deprecated replaced by QuestionIO class
	/*private ArrayList<Pergunta> leFicheiro(){
		File file = getBaseContext().getFileStreamPath(NOME_FICHEIRO);
		BufferedReader input=null;
		ArrayList<Pergunta> perguntas= new ArrayList<>();

	       if (file.exists()){
	    	   try {
	    		   String line;
	    		   String txtper;
	    		   String resp1, resp2, resp3, resp4;
	    		   String respCerta;
	    		   String oper;
	    		   input =new BufferedReader(new InputStreamReader(openFileInput(NOME_FICHEIRO)));
	    		   while(input.ready()){
	    			   line=input.readLine();
	    			   //Pergunta
	    			   String[] txt=line.split("\t");
	    			   txtper= txt[0];
	    			   //Respostas
	    			   resp1= txt[1];
	    			   resp2= txt[2];
	    			   resp3= txt[3];
	    			   resp4= txt[4];
	    			   respCerta= txt[5];
	    			   oper=txt[6];
	    			   perguntas.add(new Pergunta(txtper,resp1,resp2,resp3,resp4,respCerta,oper));
	    		   }
	    	   } catch (Exception e){
	    		   e.printStackTrace();
	    	   }finally {
	    		   if(input !=null){
	    			   try{
	    				   input.close();
	    			   }catch (IOException e){
	    				   e.printStackTrace();
	    			   }
	    		   }
	    		}
	       }
	       return perguntas;
	}*/

	private class ButtonTouchListener implements View.OnTouchListener {
		final int posicao;
		boolean novajogada= Boolean.TRUE;

		private ButtonTouchListener(int posicao) {
			this.posicao=posicao;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			File file = getBaseContext().getFileStreamPath(NOME_FICHEIRO);

			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:

					btn[0].setEnabled(false);
					btn[1].setEnabled(false);
					btn[2].setEnabled(false);
					btn[3].setEnabled(false);


					Animation anim = AnimationUtils.loadAnimation(Jogo.this, R.anim.botao_resposta);
					Jogo.this.btn[posicao].startAnimation(anim);


					if 	(posicao==Integer.valueOf(rpCerta)) {
						btn[this.posicao].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColorRight));

						if(som) mpCerto.start();
						if (!prefs.getBoolean("LeaderChanged", false)) {
							prefs.edit().putBoolean("LeaderChanged", true).apply();
						}
						numCertas++;
						txtCertas.setText(Integer.toString(numCertas));

						if (!treinar){
							myScore=myScore+nivel*tabuada.getFatorPontos();
							saveForAchievement();
							unlockAchievement();
						}

						if (treinar) {
							lstErradas.remove(numArray);
							if (numArray==0) {
								lstErradas.clear();
								novajogada= Boolean.FALSE;

								AlertDialog.Builder builder = new AlertDialog.Builder(
										Jogo.this);
								builder.setMessage(getApplicationContext().getString(R.string.NMoreQtions))
										.setCancelable(false)
										.setPositiveButton("Ok",
												new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog,
																		int id) {
														Jogo.this.finish();
													}
												});
								AlertDialog alert = builder.create();
								alert.show();

							} else {
								// quando está a treinar e ainda existem erradas na lista
								numArray--;
							}
						}
						// Acerta e não está a treinar

					} else {
						// resposta errada
							btn[this.posicao].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColorWrong));
							btn[Integer.parseInt(rpCerta)].setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.backgroundButtonColorRight));

						if (som) mpErrado.start();
						numErradas++;
						txtErradas.setText(Integer.toString(numErradas));
						if (file.length() < 450 && !treinar) {
							lstErradas.add(new Pergunta(tabuada.PtoString(), tabuada.getResposta(0), tabuada.getResposta(1), tabuada.getResposta(2), tabuada.getResposta(3), tabuada.getCerta(), tabuada.getSinal()));
						}
						/** não desconta pontos
						if (!treinar) {

							score=score-nivel*tabuada.getFatorPontos();
						} */
					}

					// nova jogada
					if (novajogada) {
						handler.postDelayed(new Runnable() {
							public void run() {
								novajogada();
							}
						}, 500);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					// Button is being touched and swiped
					break;

				case MotionEvent.ACTION_UP:
					break;
			}
			return true;

		}
	}

	private void saveForAchievement() {
		SharedPreferences.Editor edit = prefs.edit();
		//achievement 3
		if (!prefs.getBoolean("Ac3Unlocked", Boolean.FALSE)) {

			if (tabuada.getSinal().equals("n")) {
				edit.putInt("op1L3", prefs.getInt("op1L3", 0) + 1);
				edit.apply();
			}

			if (tabuada.getSinal().equals("x")) {
				edit.putInt("op2L3", prefs.getInt("op2L3", 0) + 1);
				edit.apply();
			}

				if (tabuada.getSinal().equals("+")) {
					edit.putInt("op3L3", prefs.getInt("op3L3", 0) + 1);
					edit.apply();
				}

				if (tabuada.getSinal().equals("-")) {
					edit.putInt("op4L3", prefs.getInt("op4L3", 0) + 1);
					edit.apply();
				}

				if (tabuada.getSinal().equals("*")) {
					edit.putInt("op5L3", prefs.getInt("op5L3", 0) + 1);
					edit.apply();
				}
				if (tabuada.getSinal().equals("m")) {
					edit.putInt("op6L3", prefs.getInt("op6L3", 0) + 1);
					edit.apply();
				}
				if (tabuada.getSinal().equals("ma")) {
					edit.putInt("op7L3", prefs.getInt("op7L3", 0) + 1);
					edit.apply();
				}
				if (tabuada.getSinal().equals(":")) {
					edit.putInt("op8L3", prefs.getInt("op8L3", 0) + 1);
					edit.apply();
				}
			}

			// Achievement 4
		if (!prefs.getBoolean("Ac4Unlocked", Boolean.FALSE)) {
				if (tabuada.getSinal().equals("n") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op1L4", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals("x") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op2L4", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("+") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op3L4", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("-") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op4L4", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("*") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op5L4", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals("m") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op6L4", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals("ma") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op7L4", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals(":") && numErradas == 0 && numCertas >= 10) {
					edit.putBoolean("op8L4", Boolean.TRUE);
					edit.apply();
				}
			}

			// achievement 5
		if (!prefs.getBoolean("Ac5Unlocked", Boolean.FALSE)) {
				if (tabuada.getSinal().equals("n") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op1L5", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals("x") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op2L5", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("+") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op3L5", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("-") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op4L5", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("*") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op5L5", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("m") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op6L5", Boolean.TRUE);
					edit.apply();
				}

				if (tabuada.getSinal().equals("ma") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op7L5", Boolean.TRUE);
					edit.apply();
				}
				if (tabuada.getSinal().equals(":") && numErradas == 0 && numCertas >= 20) {
					edit.putBoolean("op8L5", Boolean.TRUE);
					edit.apply();
				}
			}
	}

	private void unlockAchievement() {
       /* Achievements
                1 the beginning - fazer 50 pontos.
                2 don't stop - 10 respostas certas seguidas na opção um.
                3 ten in each option - 10 respostas corretas em cada opção.
                4 ten in a row - 10 respostas corretas seguidas em cada opção.
                5 Addicted 20 respostas corretas seguidas em cada opção.*/

		SharedPreferences.Editor edit = prefs.edit();

		// primeira conquista...
		// a primeira conquista é desbloqueada sem armazenamento de variáveis.

		if (!prefs.getBoolean("Ac1Unlocked", Boolean.FALSE) && myScore>=50) {
			edit.putBoolean("Ac1Unlocked", Boolean.TRUE).apply();

		}

		// segunda conquista...
		// a segunda conquista é desbloqueada sem armazenamento de variáveis.
		if (!prefs.getBoolean("Ac2Unlocked",Boolean.FALSE) && tabuada.getSinal().equals("n") && numErradas==0 && numCertas>=10){
			edit.putBoolean("Ac2Unlocked", Boolean.TRUE).apply();

		}

		// terceira conquista...
		if (!prefs.getBoolean("Ac3Unlocked",Boolean.FALSE) && prefs.getInt("op1L3", 0)>=10 && prefs.getInt("op2L3",0)>=10
				&& prefs.getInt("op3L3",0)>=10 && prefs.getInt("op4L3",0)>=10 && prefs.getInt("op5L3",0)>=10 && prefs.getInt("op6L3",0)>=10
				&& prefs.getInt("op7L3",0)>=10 && prefs.getInt("op8L3",0)>=10){
			edit.putBoolean("Ac3Unlocked", Boolean.TRUE).apply();

		}

		// quarta conquista...
		if (!prefs.getBoolean("Ac4Unlocked",Boolean.FALSE) && prefs.getBoolean("op1L4", false) && prefs.getBoolean("op2L4",false)
				&& prefs.getBoolean("op3L4",false)&& prefs.getBoolean("op4L4",false) && prefs.getBoolean("op5L4",false)
				&& prefs.getBoolean("op6L4",false)&& prefs.getBoolean("op7L4",false)&& prefs.getBoolean("op8L4",false)){
			edit.putBoolean("Ac4Unlocked", Boolean.TRUE).apply();

		}
		// quinta  conquista...
		if (!prefs.getBoolean("Ac5Unlocked",Boolean.FALSE) && prefs.getBoolean("op1L5",false) && prefs.getBoolean("op2L5",false)
				&& prefs.getBoolean("op3L5",false) && prefs.getBoolean("op4L5",false) && prefs.getBoolean("op5L5",false)
				&& prefs.getBoolean("op6L5",false)&& prefs.getBoolean("op7L5",false) && prefs.getBoolean("op7L5",false)){
			edit.putBoolean("Ac5Unlocked",Boolean.TRUE).apply();

		}
	}

}
