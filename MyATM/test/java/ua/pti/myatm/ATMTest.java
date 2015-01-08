/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;
import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 *
 * @author Sasha Makoveets
 */
public class ATMTest {
    /** private static final double INCORRECT_AMOUNT_OF_MONEY = -1000.0;*/
    private static final double MONEY = 1000.0;
    private static final int DEFAULT_PIN = 1111;
    private static final double BALANCE = 500.0;

    @Test(expected = NotAppropriateAmount.class)
    public void testSetIncorrectAmountOfMoneyInATM() throws NotAppropriateAmount {
        System.out.println("Set incorrect amount of money in the ATM");
        double AMOUNT_OF_MONEY = -1000.0;
        ATM atm = new ATM(AMOUNT_OF_MONEY);
    }



    @Test(expected = NotAppropriateAmount.class)
    public void testZeroAmountOfMoneyInATM() throws  NotAppropriateAmount {
        System.out.println("0 amount of money in the ATM");
        double AMOUNT_OF_MONEY = 0.0;
        ATM atm = new ATM(AMOUNT_OF_MONEY);
    }


    @Test
    public void testGetInformationAboutMoneyInATM() throws NotAppropriateAmount {
        System.out.println("Get information about money in the ATM");

        ATM atm = new ATM(MONEY);
        assertEquals(MONEY, atm.getMoneyInATM(), 0.0);
    }

    @Test
    public void testCardIsBlocked() throws NoCardInserted, NotAppropriateAmount {
        System.out.println("Card is blocked");

        boolean resultValidateCard;


        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(true);

        ATM atm = new ATM(MONEY);
        resultValidateCard = atm.validateCard(card, DEFAULT_PIN);


        verify(card).isBlocked();

        assertFalse(resultValidateCard);

    }

    @Test
    public void testPinIsWrong() throws NoCardInserted, NotAppropriateAmount {
        System.out.println("PIN is wrong");

        boolean resultValidateCard;
        boolean resultThereIsNoCardInATM;

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(false);

        InOrder inOrder = inOrder(card);

        ATM atm = new ATM(MONEY);
        resultValidateCard = atm.validateCard(card, DEFAULT_PIN);
        resultThereIsNoCardInATM = atm.thereIsNoCardInATM();

        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(DEFAULT_PIN);

        assertFalse(resultValidateCard);
        assertTrue(resultThereIsNoCardInATM);
    }

    @Test
    public void testCardIsNotBlockedAndPinIsOK() throws NoCardInserted, NotAppropriateAmount {
        System.out.println("Card in NOT blocked and PIN IS correct");

        boolean resultValidateCard;
        boolean resultThereIsNoCardInATM;

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);

        InOrder inOrder = inOrder(card);

        ATM atm = new ATM(MONEY);
        resultValidateCard = atm.validateCard(card, DEFAULT_PIN);
        resultThereIsNoCardInATM = atm.thereIsNoCardInATM();

        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(DEFAULT_PIN);

        assertTrue(resultValidateCard);
        assertFalse(resultThereIsNoCardInATM);
    }

    @Test(expected = NoCardInserted.class)
    public void testCheckBalanceWhenThereIsNoCardInside() throws NoCardInserted, NotAppropriateAmount {
        System.out.println("Check the balance when there is NO card inside");

        ATM atm = new ATM(MONEY);
        atm.checkBalance();
    }

    @Test
    public void testCheckBalanceWhenThereIsCardInside() throws NoCardInserted, NotAppropriateAmount {
        System.out.println("Check the balance when there IS a card inside");

        double resultCheckBalance;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(BALANCE);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);
        when(card.getAccount()).thenReturn(account);

        InOrder inOrder = inOrder(card,account);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card,DEFAULT_PIN);
        resultCheckBalance = atm.checkBalance();

        // atm.validateCard(card,DEFAULT_PIN)
        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(DEFAULT_PIN);
        // atm.checkBalance()
        inOrder.verify(card).getAccount();
        inOrder.verify(account).getBalance();

        assertEquals(BALANCE, resultCheckBalance, 0.0);
    }

    @Test(expected = NoCardInserted.class)
    public void testGetCashWhenThereIsNoCardInside() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is NO card inside");

        double amount = 100.0;

        ATM atm = new ATM(MONEY);
        atm.getCash(amount);
    }






    @Test(expected = NotAppropriateAmount.class)
    public void testGetCashWhenThereIsNotAppropriateAmountSet() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is NOT appropriate amount of money set");

        double amount = -100.0;

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);

        InOrder inOrder = inOrder(card);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card, DEFAULT_PIN);

        try {
            atm.getCash(amount);
        } catch (NotAppropriateAmount ex) {
            inOrder.verify(card).isBlocked();
            inOrder.verify(card).checkPin(DEFAULT_PIN);
            throw ex;
        }
    }



    @Test(expected = NotAppropriateAmount.class)
    public void testGetCashWhenThereIsZeroMoneyAmountInside() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is 0 amount of money set");

        double amount = 0.0;

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);

        InOrder inOrder = inOrder(card);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card, DEFAULT_PIN);

        try {
            atm.getCash(amount);
        } catch (NotAppropriateAmount ex) {
            inOrder.verify(card).isBlocked();
            inOrder.verify(card).checkPin(DEFAULT_PIN);
            throw ex;
        }

    }




    @Test(expected = NotEnoughMoneyInAccount.class)
    public void testGetCashWhenSetAmountBiggerThenInAccount() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is NOT appropriate amount of money set (Bigger than there is in the account)");

        double amount = BALANCE + 100.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(BALANCE);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);
        when(card.getAccount()).thenReturn(account);

        InOrder inOrder = inOrder(card,account);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card,DEFAULT_PIN);

        try {
            atm.getCash(amount);
        } catch (NotEnoughMoneyInAccount ex) {
            inOrder.verify(card).isBlocked();
            inOrder.verify(card).checkPin(DEFAULT_PIN);
            inOrder.verify(card).getAccount();
            inOrder.verify(account).getBalance();
            throw ex;
        }
    }

    @Test(expected = NotEnoughMoneyInATM.class)
    public void testGetCashWhenSetAmountBiggerThenInATM() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is NOT appropriate amount of money set (Bigger than there is in the ATM)");

        double amount = MONEY + 100.0;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(BALANCE + MONEY);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);
        when(card.getAccount()).thenReturn(account);

        InOrder inOrder = inOrder(card,account);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card,DEFAULT_PIN);

        try {
            atm.getCash(amount);
        } catch (NotEnoughMoneyInATM ex) {
            inOrder.verify(card).isBlocked();
            inOrder.verify(card).checkPin(DEFAULT_PIN);
            inOrder.verify(card).getAccount();
            inOrder.verify(account).getBalance();
            throw ex;
        }
    }

    @Test
    public void testGetCashWhenEverythingOK() throws NoCardInserted, NotAppropriateAmount, NotEnoughMoneyInAccount, NotEnoughMoneyInATM {
        System.out.println("Get cash when there is everything OK");

        double amount = 100.0;
        double result;
        double expResult;

        Account account = mock(Account.class);
        when(account.getBalance()).thenReturn(BALANCE);
        when(account.withdrow(amount)).thenReturn(amount);

        Card card = mock(Card.class);
        when(card.isBlocked()).thenReturn(false);
        when(card.checkPin(DEFAULT_PIN)).thenReturn(true);
        when(card.getAccount()).thenReturn(account);

        InOrder inOrder = inOrder(card,account);

        ATM atm = new ATM(MONEY);
        atm.validateCard(card,DEFAULT_PIN);
        expResult = MONEY - amount;
        atm.getCash(amount);
        result = atm.getMoneyInATM();

        // atm.validateCard(card,DEFAULT_PIN)
        inOrder.verify(card).isBlocked();
        inOrder.verify(card).checkPin(DEFAULT_PIN);
        // atm.getCash()
        inOrder.verify(card).getAccount();
        inOrder.verify(account).getBalance();
        inOrder.verify(account).withdrow(amount);
        inOrder.verify(account).getBalance();

        // There is no sense in this check, because we don't have realization of the Account.checkBalance() function
        // assertEquals(BALANCE - amount, atm.getCash(amount), 0.0);
        assertEquals(expResult, result, 0.0);
    }
}
