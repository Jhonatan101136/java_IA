package Interface.velha;

import game.ia.Velha_IA;
import game.ia.velha_classe_ia;
import velha.UX.JogoMonitor;

public class jogoServiceLogc implements jogoService {

	private Velha_IA velha_IA;

	private velha_classe_ia velha_classe;

	private JogoMonitor jogoMonitor;

	public jogoServiceLogc() {
		velha_IA = new Velha_IA();
		velha_classe = new velha_classe_ia();
		jogoMonitor = new JogoMonitor(velha_IA, this);
	}

	@Override
	public void playerFezUmaJogada(final int posicao) {
		realizaJogada(posicao);
		velha_classe.setSituacao(Velha_IA.status_aguarde);
		setInformacoesDoJogo();
	}

	@Override
	public void vezDaVelhaJogar() {
		final int posicao = velha_IA.getJogada();

		if (posicao < 0) {
			return;
		}

		velha_classe.setSituacao(Velha_IA.status_aguarde);
		realizaJogada(posicao);
		setInformacoesDoJogo();
	}

	@Override
	public void startPartida(final int dificudade) {
		velha_IA.setDificudade(getDificudade(dificudade));
		limparTela();
		JogoMonitor.game_startado = true;
		velha_classe.setSituacao(Velha_IA.status_aguarde);
		setInformacoesDoJogo();
	}

	private int getDificudade(final int dificudade) {
		if (dificudade == 0) {
			return 10;
		}

		if (dificudade == 1) {
			return 5;
		}

		return 0;
	}

	private void realizaJogada(final int posicao) {
		if ((velha_IA.getPosicao(posicao) != Velha_IA.vazio)
				|| velha_classe.getSituacao().equals(Velha_IA.status_aguarde) || (velha_IA.isFimDeJogo())) {
			return;
		}

		velha_classe.setId(Velha_IA.player_rodada);
		velha_IA.setPosicao(posicao, velha_classe.getId());
	}

	private void setInformacoesDoJogo() {
		String situacao = "";

		if (Velha_IA.status_aguarde.equals(velha_classe.getSituacao())) {
			situacao += "Vez da maquina jogar";
		}
		if (Velha_IA.status_sua_vez.equals(velha_classe.getSituacao())) {
			situacao += "Sua vez de jogar ";
		}
		if (Velha_IA.status_ganhador.equals(velha_classe.getSituacao())) {
			situacao += "Meus parabéns voce ganhou essa partida";
		}
		if (Velha_IA.status_perdedor.equals(velha_classe.getSituacao())) {
			situacao += "Eu que ganhei essa partida";
		}
		if (Velha_IA.status_deu_velha.equals(velha_classe.getSituacao())) {
			situacao += "Deu velha infelizmente";
		}

		jogoMonitor.setSituacaoJogo(situacao);
	}

	private void limparTela() {
		jogoMonitor.setSituacaoJogo("Pronto para iniciar a partida..");
		velha_IA.limparTabuleiro();
		velha_classe.limpar();
		jogoMonitor.CriaPainelJogo();
	}

	@Override
	public void verificaSituacaoJogo() {

		if (velha_IA.isEmpate()) {
			velha_classe.setSituacao(Velha_IA.status_deu_velha);
			setInformacoesDoJogo();
			return;
		}

		if (velha_IA.isGanhador(Velha_IA.player_x)) {
			velha_classe.setSituacao(Velha_IA.status_ganhador);
			setInformacoesDoJogo();
			return;
		}

		if (velha_IA.isGanhador(Velha_IA.player_o)) {
			velha_classe.setSituacao(Velha_IA.status_perdedor);
			setInformacoesDoJogo();
			return;
		}
	}
}
