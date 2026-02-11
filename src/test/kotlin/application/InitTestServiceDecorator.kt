package application

import com.matag.game.status.GameStatus

class InitTestServiceDecorator : InitTestService() {
    private var initTestService: InitTestService? = null

    override fun initGameStatus(gameStatus: GameStatus?) {
        initTestService!!.initGameStatus(gameStatus)
    }

    fun setInitTestService(initTestService: InitTestService) {
        initTestService.setCardInstanceFactory(this.getCardInstanceFactory())
        initTestService.setCards(this.cards)
        this.initTestService = initTestService
    }
}
