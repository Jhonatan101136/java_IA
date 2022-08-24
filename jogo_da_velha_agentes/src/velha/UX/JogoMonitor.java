package velha.UX;

import java.awt.BorderLayout;
import java.awt.Component;
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

import Interface.velha.jogoService;
import Interface.velha.jogoServiceLogc;
import game.ia.Velha_IA;

public class JogoMonitor implements ActionListener {

	private JFrame monitor;

	private frontJogo frontJogoVelha;

	private JPanel opcoesJogo;
	private JButton iniciaPartida;
	private JComboBox<String> listaDificudades;

	private JPanel informacao;
	private JLabel situacaoJogo;

	private jogoService jogoServiceVelha;

	public static boolean game_startado;

	public JogoMonitor(Velha_IA velhatIA, jogoService service) {

		jogoServiceVelha = service;

		monitor = new JFrame("Jogo da Velha utilizando conceitos de agentes inteligentes");
		monitor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		monitor.setLayout(new BorderLayout(10, 10));
		monitor.setResizable(false);

		criaPainelDeOpcoes();
		criaPainelDeInformacoes();

		frontJogoVelha = new frontJogo(velhatIA, service);

		monitor.add(opcoesJogo, BorderLayout.NORTH);
		monitor.add(frontJogoVelha, BorderLayout.CENTER);
		monitor.add(informacao, BorderLayout.SOUTH);

		monitor.setSize(650, 550);
		monitor.setLocationByPlatform(true);
		monitor.setVisible(true);
		monitor.setLocationRelativeTo(null);
	}

	public void CriaPainelJogo() {
		frontJogoVelha.repaint();
	}

	public void setSituacaoJogo(String info) {
		situacaoJogo.setText(info);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {

		if (evento.getSource() == iniciaPartida) {
			jogoServiceVelha.startPartida(listaDificudades.getSelectedIndex());
		}
	}

	private void criaPainelDeOpcoes() {

		opcoesJogo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		opcoesJogo.setBorder(new EtchedBorder());

		iniciaPartida = new JButton("Iniciar partida");
		iniciaPartida.addActionListener(this);
		opcoesJogo.add(iniciaPartida);

		final String[] dificudades = { "Básico", "Intermediário", "Avançado" };
		listaDificudades = new JComboBox<String>(dificudades);
		listaDificudades.setSelectedIndex(0);
		listaDificudades.addActionListener(this);
		opcoesJogo.add(listaDificudades);

	}

	private void criaPainelDeInformacoes() {

		informacao = new JPanel(new FlowLayout());
		informacao.setBorder(new EtchedBorder());
		situacaoJogo = new JLabel("Tudo pronto para começar a partida");
		situacaoJogo.setFont(new Font(null, Font.BOLD, 14));
		informacao.add(situacaoJogo);
	}

	public static void main(String[] args) {
		new jogoServiceLogc();
	}

}
