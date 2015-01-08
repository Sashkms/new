package ua.pti.myatm;

public class ATM {

    private double moneyInATM;
    private boolean NoCardInserted = true;
    private Card InsertedCard;

    //Можно задавать количество денег в банкомате
    ATM(double moneyInATM) throws NotAppropriateAmount {
        if (moneyInATM <= 0.0) {
            throw new NotAppropriateAmount();
        }
        this.moneyInATM = moneyInATM;
    }

    public boolean thereIsNoCardInATM() {
        return this.NoCardInserted;
    }

    // Возвращает каоличестов денег в банкомате
    public double getMoneyInATM() {
        return this.moneyInATM;
    }

    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode){
        if (card.isBlocked()) {
            this.NoCardInserted = true;
            return false;
        } else if (!card.checkPin(pinCode)) {
            this.NoCardInserted = true;
            return false;
        } else {
            this.InsertedCard = card;
            this.NoCardInserted = false;
            return  true;
        }
    }

    //Возвращает сколько денег есть на счету
    public double checkBalance() throws NoCardInserted {
        if (this.NoCardInserted) {
            throw new NoCardInserted();
        }

        return InsertedCard.getAccount().getBalance();
    }

    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount) throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NotAppropriateAmount {
        if (this.NoCardInserted) {
            throw new NoCardInserted();
        } else if (amount <= 0.0) {
            throw new NotAppropriateAmount();
        }

        Account account = this.InsertedCard.getAccount();
        double moneyInAccount = account.getBalance();

        if (amount > moneyInAccount) {
            throw new NotEnoughMoneyInAccount();
        } else if (amount > this.moneyInATM) {
            throw new NotEnoughMoneyInATM();
        }

        this.moneyInATM = this.moneyInATM - account.withdrow(amount);

        return account.getBalance();
    }
}