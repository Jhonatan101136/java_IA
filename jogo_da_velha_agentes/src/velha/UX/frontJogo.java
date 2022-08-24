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

public class frontJogo extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int largura = 400;

	private static final int altura = largura;

	private static final int X_3 = largura / 3;

	private static final int Y_3 = altura / 3;

	private static final float largura_row_tabuleiro = 4.0f;

	private static final Paint cor_tabuleiro = Color.BLUE;

	private static final float linha_vencedora = 13.5f;

	private static final Paint cor_linha_vencedora = Color.green;

	private static final float largura_linha_player = 8.0f;

	private static final Paint cor_player_x = Color.RED;

	private static final Paint cor_player_o = Color.BLUE;

	private static final int espaco_entre_player = largura / 25;

	private int x_inicio = 1;

	private int y_inicio = 0;

	private int x_finish = x_inicio + largura;

	private int y_finish = y_inicio + altura;

	private Point posicoesTabuleiro[][] = new Point[9][2];

	private Velha_IA velhaIA = null;

	private jogoService jogo_service_velha = null;

	public frontJogo(Velha_IA velhaIA, jogoService service) {

		this.velhaIA = velhaIA;
		this.jogo_service_velha = service;

		calcularCoordenadasDasPosicoes();

		addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent evento) {
				if (velhaIA.isFimDeJogo() || !JogoMonitor.game_startado) {
					return;
				}

				int posicao = getPosicao(evento.getX(), evento.getY());
				if (posicao >= 0) {
					jogo_service_velha.playerFezUmaJogada(posicao);
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

	@Override
	public void paintComponent(final Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2D = (Graphics2D) graphics;

		calcularCoordenadasDasPosicoes();

		desenharTabuleiro(graphics2D);
		desenharJogadas(graphics2D);
		desenharJogadaVencedora(graphics2D);
	}

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

	private int getPosicao(final int posicaoX, final int posicaoY) {

		for (int posicao = 0; posicao < posicoesTabuleiro.length; posicao++) {

			final Point posicaoInicial = posicoesTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesTabuleiro[posicao][1];

			if (posicaoX > posicaoInicial.x && posicaoX < posicaoFinal.x && posicaoY > posicaoInicial.y
					&& posicaoY < posicaoFinal.y) {
				return posicao;
			}
		}

		return -1;
	}

	private void desenharTabuleiro(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(largura_row_tabuleiro));
		graphics2D.setPaint(cor_tabuleiro);

		graphics2D.drawLine(x_inicio, y_inicio + Y_3, x_finish, y_inicio + Y_3);
		graphics2D.drawLine(x_inicio, y_finish - Y_3, x_finish, y_finish - Y_3);

		graphics2D.drawLine(x_inicio + X_3, y_inicio, x_inicio + X_3, y_finish);
		graphics2D.drawLine(x_finish - X_3, y_inicio, x_finish - X_3, y_finish);
	}

	private void desenharJogadas(final Graphics2D graphics2D) {

		graphics2D.setStroke(new BasicStroke(largura_linha_player));

		for (int posicao = 0; posicao < 9; posicao++) {

			final Point posicaoInicial = posicoesTabuleiro[posicao][0];
			final Point posicaoFinal = posicoesTabuleiro[posicao][1];

			final int posicaoX1 = posicaoInicial.x + espaco_entre_player;
			final int posicaoX2 = posicaoFinal.x - espaco_entre_player;
			final int posicaoY1 = posicaoInicial.y + espaco_entre_player;
			final int posicaoY2 = posicaoFinal.y - espaco_entre_player;

			if (velhaIA.getPosicao(posicao) == Velha_IA.player_x) {

				graphics2D.setPaint(cor_player_x);
				graphics2D.drawLine(posicaoX1, posicaoY1, posicaoX2, posicaoY2);
				graphics2D.drawLine(posicaoX1, posicaoY2, posicaoX2, posicaoY1);

			} else if (velhaIA.getPosicao(posicao) == Velha_IA.player_o) {

				graphics2D.setPaint(cor_player_o);
				graphics2D.drawOval(posicaoX1, posicaoY1, posicaoX2 - posicaoX1, posicaoY2 - posicaoY1);

			}
		}
	}

	private void desenharJogadaVencedora(final Graphics2D grphics2D) {

		int jogadaVencedora[] = velhaIA.getPosicoesGanhadoras();

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
