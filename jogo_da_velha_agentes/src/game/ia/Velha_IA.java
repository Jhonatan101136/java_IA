package game.ia;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Velha_IA {

	public static final char player_x = 'X';

	public static final char player_o = 'O';

	public static final char vazio = ' ';

	public static char player_rodada = player_x;

	public static final String status_aguarde = "Aguarde a sua vez de jogar";

	public static final String status_sua_vez = "Vamos lá é a sua vez";

	public static final String status_ganhador = "Parabéns voce é o vencedor";

	public static final String status_perdedor = "Que pena voce perdeu";

	// private Velha_IA velhaIA = null;

	public static final String status_deu_velha = "Deu velha o jogo ganhou";

	private final int Vencedores[][] = { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
			{ 0, 4, 8 }, { 2, 4, 6 } };

	private char[] tabuleiro = new char[9];

	private int dificudade;

	public Velha_IA() {
		limparTabuleiro();
	}

	public static char getOponente(final char jogador) {
		switch (jogador) {
		case player_x: {
			return player_o;
		}
		case player_o: {
			return player_x;
		}
		default: {
			return vazio;
		}
		}
	}

	public char getPosicao(final int posicao) {
		return tabuleiro[posicao];
	}

	public void setPosicao(final int pos, final char jogador) {
		if (jogador == player_o || jogador == player_x) {
			tabuleiro[pos] = jogador;
		}
		alteraJogadorDaVez(jogador);
	}

	private void alteraJogadorDaVez(final char jogadorDaVez) {
		player_rodada = jogadorDaVez == player_x ? player_o : player_x;
	}

	public void limparTabuleiro() {
		player_rodada = player_x;
		for (int pos = 0; pos < tabuleiro.length; pos++) {
			tabuleiro[pos] = vazio;
		}
	}

	public boolean isFimDeJogo() {
		return (isCheio() || isGanhador(player_x) || isGanhador(player_o));
	}

	public char getGanhador() {
		int posicoesGanhadoras[] = getPosicoesGanhadoras();

		if (posicoesGanhadoras == null || posicoesGanhadoras.length < 3) {
			return vazio;
		} else {
			return tabuleiro[posicoesGanhadoras[0]];
		}
	}

	public int[] getPosicoesGanhadoras() {
		int posicaoGanhadoras[] = null;
		int posicao[] = new int[3];
		char jogada[] = new char[3];

		for (int indice = 0; indice < Vencedores.length; indice++) {
			for (int ponto = 0; ponto < 3; ponto++) {
				posicao[ponto] = Vencedores[indice][ponto];
				jogada[ponto] = tabuleiro[posicao[ponto]];
			}

			if (jogada[0] != vazio && jogada[0] == jogada[1] && jogada[1] == jogada[2]) {
				posicaoGanhadoras = new int[3];
				for (int ponto = 0; ponto < 3; ponto++) {
					posicaoGanhadoras[ponto] = posicao[ponto];
				}
			}
		}
		return posicaoGanhadoras;
	}

	public boolean isGanhador(final char jogador) {
		if (vazio == getGanhador()) {
			return false;
		}
		return getGanhador() == jogador;
	}

	public boolean isEmpate() {
		return isCheio() && getGanhador() == vazio;
	}

	public boolean isvazio() {
		return isvazioEntre(0, tabuleiro.length - 1);
	}

	/*
	 * logica para jogadas
	 * 'http://pt.wikipedia.org/wiki/Jogo_da_velha#Jogada_perfeita'.
	 */
	public int getJogada() {
		int jogada = -1;

		jogada = getJogadaAtaque(player_rodada);
		if (jogada >= 0) {
			return jogada;
		}

		if (new Random().nextInt(10) >= getDificudade()) {
			jogada = getJogadaDefesa(player_rodada);
			if (jogada >= 0) {
				return jogada;
			}
		}

		if (isvazio() && new Random().nextBoolean()) {
			jogada = getJogadaCanto(player_rodada);
			if (jogada >= 0) {
				return jogada;
			}
		}

		jogada = getJogadaAtaqueTriangulo(player_rodada);
		if (jogada >= 0) {
			return jogada;
		}

		if (new Random().nextInt(10) >= getDificudade()) {
			jogada = getJogadaDefesaTriangulo(player_rodada);
			if (jogada >= 0)
				return jogada;
		}

		if (tabuleiro[4] == vazio) {
			jogada = 4;
			return jogada;
		}

		jogada = getJogadaCanto(player_rodada);
		if (jogada >= 0)
			return jogada;

		jogada = getJogadaBorda(player_rodada);
		if (jogada >= 0)
			return jogada;

		return -1;
	}

	private boolean isvazioEntre(int inicio, int fim) {
		if (inicio > fim) {
			int inicioTemporario = inicio;
			inicio = fim;
			fim = inicioTemporario;
		}

		if (inicio < 0) {
			inicio = 0;
		}

		if (fim >= tabuleiro.length) {
			fim = tabuleiro.length - 1;
		}

		for (int posicao = inicio; posicao <= fim; posicao++) {
			if (tabuleiro[posicao] != vazio) {
				return false;
			}
		}

		return true;
	}

	public boolean isCheio() {
		for (int posicao = 0; posicao < tabuleiro.length; posicao++) {
			if (tabuleiro[posicao] == vazio)
				return false;
		}
		return true;
	}

	private int getJogadaAtaque(final char jogador) {

		List<Integer> jogadasPossiveis = new ArrayList<Integer>();
		for (int posicao = 0; posicao < 9; posicao++) {
			if (tabuleiro[posicao] == vazio) {
				jogadasPossiveis.add(posicao);
			}
		}

		List<Integer> boasJogadas = getMelhoresJogadas(jogadasPossiveis, new char[] { vazio, jogador, jogador });

		if (boasJogadas.size() > 0) {
			return boasJogadas.get(0);
		}

		return -1;
	}

	private int getJogadaDefesa(final char jogador) {
		return getJogadaAtaque(getOponente(jogador));
	}

	private int getJogadaDefesaTriangulo(final char jogador) {
		if (isJogarNaBorda(jogador)) {
			return getJogadaBorda(jogador);
		}

		if (isJogadorNoCentroEBordasVazias(jogador)) {

			if (isJogarNaPosicao2ParaBordasVazias(jogador)) {
				return 2;
			}

			if (isJogarNaPosicao8ParaBordasVazias(jogador)) {
				return 8;
			}

			if (isJogarNaPosicao6ParaBordasVazias(jogador)) {
				return 6;
			}

			if (isJogarNaPosicao0ParaBordasVazias(jogador)) {
				return 0;
			}
		}

		if (tabuleiro[4] == jogador) {

			if (isJogarNaPosicao2(jogador)) {
				return 2;
			}

			if (isJogarNaPosicao8(jogador)) {
				return 8;
			}

			if (isJogarNaPosicao6(jogador)) {
				return 6;
			}

			if (isJogarNaPosicao0(jogador)) {
				return 0;
			}
		}

		return -1;
	}

	private boolean isJogarNaPosicao0(final char jogador) {
		return tabuleiro[0] == vazio && tabuleiro[1] == getOponente(jogador) && tabuleiro[2] == vazio
				&& tabuleiro[3] == vazio && tabuleiro[5] == vazio && tabuleiro[6] == getOponente(jogador)
				&& tabuleiro[7] == vazio && tabuleiro[8] == vazio;
	}

	private boolean isJogarNaPosicao6(final char jogador) {
		return tabuleiro[0] == vazio && tabuleiro[1] == vazio && tabuleiro[2] == vazio
				&& tabuleiro[3] == getOponente(jogador) && tabuleiro[5] == vazio && tabuleiro[6] == vazio
				&& tabuleiro[7] == vazio && tabuleiro[8] == getOponente(jogador);
	}

	private boolean isJogarNaPosicao8(final char jogador) {
		return tabuleiro[0] == vazio && tabuleiro[1] == vazio && tabuleiro[2] == getOponente(jogador)
				&& tabuleiro[3] == vazio && tabuleiro[5] == vazio && tabuleiro[6] == vazio
				&& tabuleiro[7] == getOponente(jogador) && tabuleiro[8] == vazio;
	}

	private boolean isJogarNaPosicao2(final char jogador) {
		return tabuleiro[0] == getOponente(jogador) && tabuleiro[1] == vazio && tabuleiro[2] == vazio
				&& tabuleiro[3] == vazio && tabuleiro[5] == getOponente(jogador) && tabuleiro[6] == vazio
				&& tabuleiro[7] == vazio && tabuleiro[8] == vazio;
	}

	private boolean isJogadorNoCentroEBordasVazias(final char jogador) {
		return tabuleiro[4] == jogador && tabuleiro[0] == vazio && tabuleiro[2] == vazio && tabuleiro[6] == vazio
				&& tabuleiro[8] == vazio;
	}

	private boolean isJogarNaPosicao0ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == getOponente(jogador) && tabuleiro[3] == getOponente(jogador) && tabuleiro[5] == vazio
				&& tabuleiro[7] == vazio;
	}

	private boolean isJogarNaPosicao6ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == vazio && tabuleiro[3] == getOponente(jogador) && tabuleiro[5] == vazio
				&& tabuleiro[7] == getOponente(jogador);
	}

	private boolean isJogarNaPosicao8ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == vazio && tabuleiro[3] == vazio && tabuleiro[5] == getOponente(jogador)
				&& tabuleiro[7] == getOponente(jogador);
	}

	private boolean isJogarNaPosicao2ParaBordasVazias(final char jogador) {
		return tabuleiro[1] == getOponente(jogador) && tabuleiro[3] == vazio && tabuleiro[5] == getOponente(jogador)
				&& tabuleiro[7] == vazio;
	}

	private boolean isJogarNaBorda(final char jogador) {
		return (tabuleiro[0] == getOponente(jogador) && tabuleiro[1] == vazio && tabuleiro[2] == vazio
				&& tabuleiro[3] == vazio && tabuleiro[4] == jogador && tabuleiro[5] == vazio && tabuleiro[6] == vazio
				&& tabuleiro[7] == vazio && tabuleiro[8] == getOponente(jogador))
				|| (tabuleiro[0] == vazio && tabuleiro[1] == vazio && tabuleiro[2] == getOponente(jogador)
						&& tabuleiro[3] == vazio && tabuleiro[4] == jogador && tabuleiro[5] == vazio
						&& tabuleiro[6] == getOponente(jogador) && tabuleiro[7] == vazio && tabuleiro[8] == vazio);
	}

	private int getJogadaAtaqueTriangulo(final char jogador) {
		if (tabuleiro[4] != getOponente(jogador) || tabuleiro[1] != vazio || tabuleiro[3] != vazio
				|| tabuleiro[5] != vazio || tabuleiro[7] != vazio) {
			return -1;
		}

		if (tabuleiro[0] == jogador && isvazioEntre(1, 2) && isvazioEntre(6, 8)) {
			return 8;
		}

		if (isvazioEntre(0, 1) && tabuleiro[2] == jogador && isvazioEntre(6, 8)) {
			return 6;
		}

		if (isvazioEntre(0, 2) && tabuleiro[6] == jogador && isvazioEntre(7, 8)) {
			return 2;
		}

		if (isvazioEntre(0, 2) && isvazioEntre(6, 7) && tabuleiro[8] == jogador) {
			return 0;
		}

		return -1;
	}

	private int getJogadaCanto(final char jogador) {
		final List<Integer> jogadasPossiveis = new ArrayList<Integer>();

		if (tabuleiro[0] == vazio) {
			jogadasPossiveis.add(0);
		}

		if (tabuleiro[2] == vazio) {
			jogadasPossiveis.add(2);
		}

		if (tabuleiro[6] == vazio) {
			jogadasPossiveis.add(6);
		}

		if (tabuleiro[8] == vazio) {
			jogadasPossiveis.add(8);
		}

		return escolherMelhorJogada(jogador, jogadasPossiveis);
	}

	private int getJogadaBorda(final char jogador) {
		final List<Integer> jogadasPossiveis = new ArrayList<Integer>();

		if (tabuleiro[1] == vazio) {
			jogadasPossiveis.add(1);
		}

		if (tabuleiro[3] == vazio) {
			jogadasPossiveis.add(3);
		}

		if (tabuleiro[5] == vazio) {
			jogadasPossiveis.add(5);
		}

		if (tabuleiro[7] == vazio) {
			jogadasPossiveis.add(7);
		}

		return escolherMelhorJogada(jogador, jogadasPossiveis);
	}

	private List<Integer> getMelhoresJogadas(final List<Integer> jogadasPossiveis, final char[] padrao) {

		final List<Integer> melhoresJogadas = new ArrayList<Integer>();

		if (padrao.length < 3) {
			return melhoresJogadas;
		}

		for (Integer jogada : jogadasPossiveis) {

			for (int posicao = 0; posicao < Vencedores.length; posicao++) {

				for (int ponto1 = 0; ponto1 < 3; ponto1++) {

					if (Vencedores[posicao][ponto1] == jogada) {

						int ponto2 = (ponto1 == 2) ? 0 : (ponto1 + 1);
						int ponto3 = (ponto2 == 2) ? 0 : (ponto2 + 1);

						if (tabuleiro[Vencedores[posicao][ponto1]] == padrao[0]
								&& tabuleiro[Vencedores[posicao][ponto2]] == padrao[1]
								&& tabuleiro[Vencedores[posicao][ponto3]] == padrao[2]) {

							melhoresJogadas.add(Vencedores[posicao][ponto1]);
						}
					}
				}
			}
		}

		return melhoresJogadas;
	}

	private int escolherMelhorJogada(final char jogador, final List<Integer> jogadasPossiveis) {

		if (jogadasPossiveis.size() < 1) {
			return -1;
		}

		if (jogadasPossiveis.size() == 1) {
			return jogadasPossiveis.get(0);
		}

		List<Integer> boasJogadas = new ArrayList<Integer>();

		boasJogadas.addAll(getMelhoresJogadas(jogadasPossiveis, new char[] { vazio, vazio, jogador }));
		boasJogadas.addAll(getMelhoresJogadas(jogadasPossiveis, new char[] { vazio, jogador, vazio }));

		if (boasJogadas.size() > 0) {
			return boasJogadas.get(new Random().nextInt(boasJogadas.size()));
		}

		boasJogadas = getMelhoresJogadas(jogadasPossiveis, new char[] { vazio, vazio, vazio });

		if (boasJogadas.size() > 0) {
			return boasJogadas.get(new Random().nextInt(boasJogadas.size()));
		}

		return jogadasPossiveis.get(new Random().nextInt(jogadasPossiveis.size()));
	}

	public int getDificudade() {
		return dificudade;
	}

	public void setDificudade(int dificudade) {
		this.dificudade = dificudade;
	}
}
