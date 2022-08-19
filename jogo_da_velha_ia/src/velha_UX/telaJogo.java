package velha_UX;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import br.unisul.ia.service.VelhaService;
import br.unisul.ia.service.VelhaServiceImpl;
import br.unisul.ia.ui.PainelJogoDaVelha;
import br.unisul.ia.velha.VelhaIA;
import velha_ia.velha_logica2;

// Montando tela

public class telaJogo implements ActionListener {
	
	private JFrame tela;
	
	//localizacao do jogo
	
	private telaCompletaJogo telaCompletaJogo;

	// opcoes do jogo
	
	private JPanel painelOpcoes;
	private JButton inicar;
	private JComboBox<String> nivelDificuldade;
	
	//situacao jogo
	
	private JPanel informacao;
	private JLabel statusJogo;
	
	//servico jogo
	
	private servicoVelho servicoVelho;
	
	// Jogo iniciado
	
	public static boolean jogoStrarttado;
	
	public telaJogo(velha_logica2 velhaLogica2, servicoVelho servico) {

		servicoVelho = servico;

		tela = new JFrame("Jogo da Velha IA");
		tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tela.setLayout(new BorderLayout(10, 10));
		tela.setResizable(false);
		
		
		criaPainelDeOpcoes();
		criaPainelDeInformacoes();

		telaCompletaJogo = new TelaCompletaJogo(velhaLogica2, servico);

		tela.add(painelOpcoes, BorderLayout.NORTH);
		tela.add(painelOpcoes, BorderLayout.CENTER);
		tela.add(painelOpcoes, BorderLayout.SOUTH);

		tela.setSize(650, 550);
		tela.setLocationByPlatform(true);
		tela.setVisible(true);
		tela.setLocationRelativeTo(null);
	}

	
	public void redesenhaPainelDoJogo() {
		telaCompletaJogo.repaint();
	}

	
	public void setSituacaoDoJogo(String info) {
		statusJogo.setText(info);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {

		if (evento.getSource() == inicar) {
			servicoVelho.onIniciarPartida(nivelDificuldade.getSelectedIndex());
		}
	}

	
	private void geraPainelOpcoes() {

		painelOpcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		painelOpcoes.setBorder(new EtchedBorder());

		inicar = new JButton("Iniciar partida");
		inicar.addActionListener(this);
		painelOpcoes.add(inicar);
		
		final String[] dificudades = {"INICIANTE", "INTERMEDIÁRIO", "AVANCADO"};
		nivelDificuldade = new JComboBox<String>(dificudades);
		nivelDificuldade.setSelectedIndex(0);
		nivelDificuldade.addActionListener(this);
		painelOpcoes.add(nivelDificuldade);
		
	}

	//Painel com informacoes
	
	private void criaPainelDeInformacoes() {

		informacao = new JPanel(new FlowLayout());
		informacao.setBorder(new EtchedBorder());
		statusJogo = new JLabel("Pronto para iniciar a partida..");
		statusJogo.setFont(new Font(null, Font.BOLD, 14));
		informacao.add(statusJogo);
	}
	
	 //inicia a aplicacao
	
	public static void main(String[] args) {
		new ServicoVelhoIm();
	}
}
