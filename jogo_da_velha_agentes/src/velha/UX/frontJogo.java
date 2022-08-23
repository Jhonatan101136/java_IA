package velha.UX;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Interface.velha.jogoService;
import game.ia.Velha_IA;

/**
 * Classe que implementa um painel Swing com o tabuleiro do Jogo da Velha.
 */
public class frontJogo extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 *  Largura do tabuleiro. 
	 */
	private static final int largura = 400;

	/**
	 *  Altura do tabuleiro. 
	 */
	private static final int altura = largura;

	/**
	 *  Largura de uma posição do tabuleiro 
	 */
	private static final int X_3 = largura / 3;

	/**
	 *  Altura de uma posicao do tabuleiro 
	 */
	private static final int Y_3 = altura / 3;

	/**
	 *  Largura da linha que desenha o tabuleiro, em pixels 
	 */
	private static final float largura_row_tabuleiro = 4.0f;

	/**
	 *  Cor do tabuleiro 
	 */
	private static final Paint cor_tabuleiro = Color.BLACK;

	/**
	 *  Largura da linha que marca a jogada vencedora. 
	 */
	private static final float linha_vencedora = 10.0f;

	/**
	 *  Cor da linha que marca a jogada vencedora.
	 */
	private static final Paint cor_linha_vencedora = Color.BLACK;

	/**
	 *  Largura da linha que desenha o X ou O de uma jogada 
	 */
	private static final float largura_linha_player = 8.0f;

	/**
	 *  Cor do jogador X 
	 */
	private static final Paint cor_playerx = Color.RED;

	/**
	 *  Cor do jogador O 
	 */
	private static final Paint cor_playero = Color.BLUE;

	/**
	 *  Espaços entre a borda da posicao do tabuleiro e o X ou O 
	 */
	private static final int espaco_entre_player = largura / 25;

	/**
	 *  Coordenada X do inicio do tabuleiro 
	 */
	private int x_inicio = 0;
	

	/**
	 *  Coordenada Y do inicio do tabuleiro 
	 */
	private int y_inicio = 0;

	/**
	 *  Coordenada X do fim do tabuleiro 
	 */
	private int x_finish = x_inicio + largura;
	

	/**
	 *  Coordenada Y do fim do tabuleiro 
	 */
	private int y_finish = y_inicio + altura;

	/**
	 * Armazena um conjunto de coordenadas que representam cada posicao no
	 * tabuleiro.
	 */
	private Point posicoesTabuleiro[][] = new Point[9][2];

	/** 
	 * Lógica do jogo. 
	 */
	private Velha_IA velha_IA = null;

	/** 
	 * Serviço do jogo. 
	 */
	private jogoService jogo_service_velha = null;

	/**
	 * Construtor da classe.
	 */
	public frontJogo(Velha_IA velhaIA, jogoService service) {

		this.velha_IA = velha_IA;
		this.jogo_service_velha = service;

		calcularCoordenadasDasPosicoes();

		addMouseListener(new MouseAdapter() {
			
			/**
			 * Evento chamada quando é clicado na tela do jogo.
			 */
			public void mousePressed(MouseEvent evento) {
				if (velhaIA.isFimDeJogo() || !JogoMonitor.game_startado) {
					return;
				}
				
				int posicao = getPosicao(evento.getX(), evento.getY());
				if (posicao >= 0) {
					jogo_service_velha.playerFezUmaJogada (posicao);
					repaint();

					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							jogo_service_velha.vezDaVelhaJogar();
							repaint();
							jogo_service_velha.verificaSituacaoJogo();
						}
					});
				}

			}

		});
	}

	/**
	 *  Resenha a tela do jogo
	 */
	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;

		calcularCoordenadasDasPosicoes();

		desenharTabuleiro(graphics2D);
		desenharJogadas(graphics2D);
		desenharJogadaVencedora(graphics2D);
	}

	/**
	 * Calcula as coordenadas das posições do tabuleiro.
	 */
	private void calcularCoordenadasDasPosicoes() {
		x_inicio = (getSize().width - largura) / 2;
		y_inicio = (getSize().height - altura) / 2;
		x_finish = x_inicio + largura;
		y_finish = y_inicio + altura;

		posicoesTabuleiro[0][0] = new Point(x_inicio, y_inicio);
		posicoesTabuleiro[0][1] = new Point(x_inicio + X_3, y_inicio + Y_3);

		posicoesTabuleiro[1][0] = new Point(x_inicio + X_3, y_inicio);
		posicoesTabuleiro[1][1] = new Point(x_finish - X_3, y_inicio + Y_3);

		posicoesTabuleiro[2][0] = new Point(x_finish - X_3, y_inicio);
		posicoesTabuleiro[2][1] = new Point(x_finish, y_inicio + Y_3);

		posicoesTabuleiro[3][0] = new Point(x_inicio, y_inicio + Y_3);
		posicoesTabuleiro[3][1] = new Point(x_inicio + X_3, y_finish - Y_3);

		posicoesTabuleiro[4][0] = new Point(x_inicio + X_3, y_inicio + Y_3);
		posicoesTabuleiro[4][1] = new Point(x_finish - X_3, y_finish - Y_3);

		posicoesTabuleiro[5][0] = new Point(x_finish - X_3, y_inicio + Y_3);
		posicoesTabuleiro[5][1] = new Point(x_finish, y_finish - Y_3);

		posicoesTabuleiro[6][0] = new Point(x_inicio, y_finish - Y_3);
		posicoesTabuleiro[6][1] = new Point(x_inicio + X_3, y_finish);

		posicoesTabuleiro[7][0] = new Point(x_inicio + X_3, y_finish - Y_3);
		posicoesTabuleiro[7][1] = new Point(x_finish - X_3, y_finish);

		posicoesTabuleiro[8][0] = new Point(x_finish - X_3, y_finish - Y_3);
		posicoesTabuleiro[8][1] = new Point(x_finish, y_finish);
	}

	/**
	 * Método que retorna uma posicao no tabuleiro, a partir das coordenadas de tela (x,y).
	 */
	private int getPosicao(final int posicaoX, final int posicaoY) {
		
		for (int posicao = 0; posicao < posicoesTabuleiro.length; posicao++) {

			final Point posicaoInicial = posicoesTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesTabuleiro[posicao][1];

			if (posicaoX > posicaoInicial.x 
					&& posicaoX < posicaoFinal.x 
					&& posicaoY > posicaoInicial.y 
					&& posicaoY < posicaoFinal.y) {
				return posicao;
			}
		}
		
		return -1;
	}

	/**
	 * Desenha o tabuleiro na tela.
	 */
	private void desenharTabuleiro(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(largura_row_tabuleiro));
		graphics2D.setPaint(cor_tabuleiro);

		graphics2D.drawLine(x_inicio, y_inicio + Y_3, x_finish, y_inicio + Y_3);
		graphics2D.drawLine(x_inicio, y_finish - Y_3, x_finish, y_finish - Y_3);

		graphics2D.drawLine(x_inicio + X_3, y_inicio, x_inicio + X_3, y_finish);
		graphics2D.drawLine(x_finish - X_3, y_inicio, x_finish - X_3, y_finish);
	}

	/**
	 * Desenha as jogadas na tela.
	 */
	private void desenharJogadas(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(largura_linha_player));

		for (int posicao = 0; posicao < 9; posicao++) {

			final Point posicaoInicial = posicoesTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesTabuleiro[posicao][1];

			final int posicaoX1 = posicaoInicial.x + espaco_entre_player;
			final int posicaoX2 = posicaoFinal.x - espaco_entre_player;
			final int posicaoY1 = posicaoInicial.y + espaco_entre_player;
			final int posicaoY2 = posicaoFinal.y - espaco_entre_player;

			if (velha_IA.getPosicao(posicao) == Velha_IA.player_x) {

				/**
				 * Desenha o 'X' na tela.  
				 */
				graphics2D.setPaint(cor_playerx);
				graphics2D.drawLine(posicaoX1, posicaoY1, posicaoX2, posicaoY2);
				graphics2D.drawLine(posicaoX1, posicaoY2, posicaoX2, posicaoY1);

			} else if (velha_IA.getPosicao(posicao) == Velha_IA.player_o) {

				/**
				 * Desenha o 'O' na tela.  
				 */
				graphics2D.setPaint(cor_playero);
				graphics2D.drawOval(posicaoX1, posicaoY1, posicaoX2 - posicaoX1, posicaoY2 - posicaoY1);

			}
		}
	}

	/**
	 * Método que desenha uma linha sobre a jogada vencedora.
	 */
	private void desenharJogadaVencedora(final Graphics2D grphics2D) {

		int jogadaVencedora[] = velha_IA.getPosicoesGanhadoras();

		if (jogadaVencedora == null || jogadaVencedora.length < 3) {
			return;
		}

		final int spacing = X_3 / 2;

		final int posicaoX1 = posicoesTabuleiro[jogadaVencedora[0]][0].x + spacing;
		final int posicaoY1 = posicoesTabuleiro[jogadaVencedora[0]][0].y + spacing;
		final int posicaoX2 = posicoesTabuleiro[jogadaVencedora[2]][0].x + spacing;
		final int posicaoY2 = posicoesTabuleiro[jogadaVencedora[2]][0].y + spacing;

		grphics2D.setStroke(new BasicStroke(linha_vencedora));
		grphics2D.setPaint(cor_linha_vencedora);

		grphics2D.drawLine(posicaoX1, posicaoY1, posicaoX2, posicaoY2);
	}

}
