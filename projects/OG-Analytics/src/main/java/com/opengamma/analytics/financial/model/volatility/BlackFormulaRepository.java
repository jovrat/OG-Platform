/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.volatility;

import org.apache.commons.lang.Validate;

import com.opengamma.analytics.math.MathException;
import com.opengamma.analytics.math.function.Function1D;
import com.opengamma.analytics.math.rootfinding.BisectionSingleRootFinder;
import com.opengamma.analytics.math.rootfinding.BracketRoot;
import com.opengamma.analytics.math.statistics.distribution.NormalDistribution;
import com.opengamma.analytics.math.statistics.distribution.ProbabilityDistribution;
import com.opengamma.lang.annotation.ExternalFunction;
import com.opengamma.util.ArgumentChecker;

/**
 * This <b>SHOULD</b> be the repository for Black formulas - i.e. the price, common greeks (delta, gamma, vega) and implied volatility. Other
 * classes that have higher level abstractions (e.g. option data bundles) should call these functions.
 * As the numeraire (e.g. the zero bond p(0,T) in the T-forward measure) in the Black formula is just a multiplication factor,  all prices,
 * input/output, are <b>forward</b> prices, i.e. (spot price)/numeraire
 */
public abstract class BlackFormulaRepository {

  private static final double EPS = 1e-15;
  private static final ProbabilityDistribution<Double> NORMAL = new NormalDistribution(0, 1);
  private static final double SMALL = 1.0E-12;
  private static final int MAX_ITERATIONS = 15; //something's wrong if Newton-Raphson taking longer than this
  private static final double VOL_TOL = 1e-9; // 1 part in 100,000 basis points will do for implied vol

  /**
   * The <b>forward</b> price of an option using the Black formula
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @param isCall True for calls, false for puts
   * @return The <b>forward</b> price
   */
  @ExternalFunction
  public static double price(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall) {
    ArgumentChecker.isTrue(lognormalVol >= 0.0, "negative volatility; have {}", lognormalVol);
    if (strike < SMALL) {
      return isCall ? forward : 0.0;
    }
    final int sign = isCall ? 1 : -1;
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);
    if (Math.abs(forward - strike) < SMALL) {
      return forward * (2 * NORMAL.getCDF(sigmaRootT / 2) - 1);
    }
    if (sigmaRootT < SMALL) {
      return Math.max(sign * (forward - strike), 0.0);
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;
    return sign * (forward * NORMAL.getCDF(sign * d1) - strike * NORMAL.getCDF(sign * d2));

  }

  public static double price(final SimpleOptionData data, final double lognormalVol) {
    return data.getDiscountFactor() * price(data.getForward(), data.getStrike(), data.getTimeToExpiry(), lognormalVol, data.isCall());
  }

  /**
   * The forward (i.e. driftless) delta
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @param isCall true for call
   * @return The forward delta
   */
  @ExternalFunction
  public static double delta(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (strike < SMALL) {
      return isCall ? 1.0 : 0.0;
    }
    final int sign = isCall ? 1 : -1;
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);

    if (sigmaRootT < SMALL) {
      return (isCall ? (forward > strike ? 1.0 : 0.0) : (forward > strike ? 0.0 : -1.0));
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;

    return sign * NORMAL.getCDF(sign * d1);
  }

  public static double strikeForDelta(final double forward, final double forwardDelta, final double timeToExpiry, final double lognormalVol, final boolean isCall) {
    Validate.isTrue((isCall && forwardDelta > 0 && forwardDelta < 1) || (!isCall && forwardDelta > -1 && forwardDelta < 0), "delta out of range");

    final int sign = isCall ? 1 : -1;
    final double d1 = sign * NORMAL.getInverseCDF(sign * forwardDelta);
    return forward * Math.exp(-d1 * lognormalVol * Math.sqrt(timeToExpiry) + 0.5 * lognormalVol * lognormalVol * timeToExpiry);
  }

  /**
   * The driftless dual delta (first derivative of option price with respect to strike)
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @param isCall true for call
   * @return The dual delta
   */
  @ExternalFunction
  public static double dualDelta(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");

    final int sign = isCall ? 1 : -1;
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);

    final double d2 = Math.log(forward / strike) / sigmaRootT - 0.5 * sigmaRootT;

    return -sign * NORMAL.getCDF(sign * d2);
  }

  /**
   * The simple delta. 
   * Note that this is not the standard delta one is accustomed to. 
   * The argument of the cumulative normal is simply d = Math.log(forward / strike) / sigmaRootT 
   * 
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @param isCall true for call
   * @return The forward delta
   */
  @ExternalFunction
  public static double simpleDelta(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (strike < SMALL) {
      return isCall ? 1.0 : 0.0;
    }
    final int sign = isCall ? 1 : -1;
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);

    if (sigmaRootT < SMALL) {
      return (isCall ? (forward > strike ? 1.0 : 0.0) : (forward > strike ? 0.0 : -1.0));
    }

    final double d = Math.log(forward / strike) / sigmaRootT;

    return sign * NORMAL.getCDF(sign * d);
  }

  /**
   * The forward (i.e. driftless) gamma, 2nd order sensitivity of the forward option value to the forward. <p>
   * $\frac{\partial^2 FV}{\partial^2 f}$ 
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward gamma
   */
  @ExternalFunction
  public static double gamma(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (forward == 0 || strike == 0.0) {
      return 0.0;
    }
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);
    if (sigmaRootT == 0.0) {
      if (forward != strike) {
        return 0.0;
      }
      //The gamma is infinite in this case
      return Double.POSITIVE_INFINITY;
    }
    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;

    return NORMAL.getPDF(d1) / forward / sigmaRootT;
  }

  /**
   * The driftless dual gamma
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The dual gamma
   */
  @ExternalFunction
  public static double dualGamma(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (strike == 0) {
      return 0.0;
    }
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);
    final double d2 = Math.log(forward / strike) / sigmaRootT - 0.5 * sigmaRootT;

    return NORMAL.getPDF(d2) / strike / sigmaRootT;
  }

  /**
   * The driftless cross gamma - the sensitity of the delta to the strike $\frac{\partial^2 V}{\partial f \partial K}$
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The dual gamma
   */
  @ExternalFunction
  public static double crossGamma(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (strike == 0) {
      return 0.0;
    }
    final double sigmaRootT = lognormalVol * Math.sqrt(timeToExpiry);
    final double d2 = Math.log(forward / strike) / sigmaRootT - 0.5 * sigmaRootT;

    return -NORMAL.getPDF(d2) / forward / sigmaRootT;
  }

  /**
   * The theta (non-forward).
   *
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @param isCall true for call, false for put
   * @param interestRate the interest rate
   * @return The forward theta
   */
  @ExternalFunction
  public static double theta(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall, final double interestRate) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    final int sign = isCall ? 1 : -1;
    final double b = 0; // for now set cost of carry to 0
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;
    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;

    final double value = -forward * NORMAL.getPDF(d1) * lognormalVol / 2 / rootT - sign * (b - interestRate) * forward * NORMAL.getCDF(sign * d1) - sign * interestRate * strike *
        Math.exp(-interestRate * timeToExpiry) * NORMAL.getCDF(sign * d2);

    return value;
  }

  /**
   * The forward (i.e. driftless) theta 
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward theta
   */
  @ExternalFunction
  public static double theta(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    Validate.isTrue(lognormalVol >= 0.0, "negative vol");
    if (forward == 0 || strike == 0.0) {
      return 0.0;
    }
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;
    if (sigmaRootT == 0.0) {
      if (forward != strike) {
        return 0.0;
      }
      //The gamma is infinite in this case
      return Double.POSITIVE_INFINITY;
    }
    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;

    return -forward * NORMAL.getPDF(d1) * lognormalVol / 2 / rootT;
  }

  /**
   * The forward vega of an option, i.e. the sensitivity of the option's forward price wrt the implied volatility (which is just the the spot vega
   * divided by the the numeraire)
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward vega
   */
  @ExternalFunction
  public static double vega(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;

    if (Math.abs(forward - strike) < SMALL) {
      return forward * rootT * NORMAL.getPDF(sigmaRootT / 2);
    }

    if (sigmaRootT < SMALL || strike < SMALL) {
      return 0.0;
    }

    final double d1 = Math.log(forward / strike) / lognormalVol / rootT + 0.5 * lognormalVol * rootT;
    return forward * rootT * NORMAL.getPDF(d1);
  }

  @ExternalFunction
  public static double vega(final SimpleOptionData data, final double lognormalVol) {
    return data.getDiscountFactor() * vega(data.getForward(), data.getStrike(), data.getTimeToExpiry(), lognormalVol);
  }

  /**
   * The driftless vanna of an option, i.e. second order derivative of the option value, once to the underlying forward and once to volatility.<p>
   * $\frac{\partial^2 FV}{\partial f \partial \sigma}$
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward vanna
   */
  @ExternalFunction
  public static double vanna(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    if (forward == 0.0 || strike == 0.0) {
      return 0.0;
    }
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;

    if (sigmaRootT < SMALL || strike < SMALL) {
      return 0.0;
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;
    return -NORMAL.getPDF(d1) * d2 / lognormalVol;
  }

  /**
   * The driftless dual vanna of an option, i.e. second order derivative of the option value, once to the strike and once to volatility.
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward dual vanna
   */
  @ExternalFunction
  public static double dualVanna(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    if (forward == 0.0 || strike == 0.0) {
      return 0.0;
    }
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;

    if (sigmaRootT < SMALL || strike < SMALL) {
      return 0.0;
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;
    return NORMAL.getPDF(d2) * d1 / lognormalVol;
  }

  /**
   * The driftless vomma (aka volga) of an option, i.e. second order derivative of the option forward price with respect to the implied volatility.
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward vomma
   */
  @ExternalFunction
  public static double vomma(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    if (forward == 0.0 || strike == 0.0) {
      return 0.0;
    }
    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;

    if (sigmaRootT < SMALL || strike < SMALL) {
      return 0.0;
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;
    return forward * NORMAL.getPDF(d1) * rootT * d1 * d2 / lognormalVol;
  }

  /**
   * The driftless volga (aka vomma) of an option, i.e. second order derivative of the option forward price with respect to the implied volatility.
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param lognormalVol The log-normal volatility
   * @return The forward vomma
   */
  @ExternalFunction
  public static double volga(final double forward, final double strike, final double timeToExpiry, final double lognormalVol) {
    return vomma(forward, strike, timeToExpiry, lognormalVol);
  }

  /**
   * Get the log-normal (Black) implied volatility of an  European option 
   * @param price The <b>forward</b> price - i.e. the market price divided by the numeraire (i.e. the zero bond p(0,T) for the T-forward measure)
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param isCall true for call
   * @return log-normal (Black) implied volatility
   */
  public static double impliedVolatility(final double price, final double forward, final double strike, final double timeToExpiry, final boolean isCall) {
    final double intrinsicPrice = Math.max(0, (isCall ? 1 : -1) * (forward - strike));
    Validate.isTrue(strike > 0, "Cannot find an implied volatility when strike is zero as there is no optionality");
    //    if (price == intrinsicPrice) {
    //      return 0.0;
    //    }
    //  ArgumentChecker.isTrue(price > intrinsicPrice, "price of {} less that intrinsic price of {}", price, intrinsicPrice);
    //
    //    if (isCall) {
    //      Validate.isTrue(price < forward, "call price must be less than forward");
    //    } else {
    //      Validate.isTrue(price < strike, "put price must be less than strike");
    //    }
    final double targetPrice = price - intrinsicPrice;
    double sigmaGuess = 0.3;
    return impliedVolatility(targetPrice, forward, strike, timeToExpiry, sigmaGuess);
  }

  /**
    * Get the log-normal (Black) implied volatility of an out-the-money European option starting from an initial guess 
    * @param otmPrice The <b>forward</b> price - i.e. the market price divided by the numeraire (i.e. the zero bond p(0,T) for the T-forward measure)
    * <b>Note</b> This MUST be an OTM price - i.e. a call price for strike >= forward and a put price otherwise 
    * @param forward The forward value of the underlying
    * @param strike The Strike
    * @param timeToExpiry The time-to-expiry
    * @param volGuess a guess of the implied volatility 
    * @return log-normal (Black) implied volatility
    */
  @ExternalFunction
  public static double impliedVolatility(final double otmPrice, final double forward, final double strike, final double timeToExpiry, final double volGuess) {
    if (otmPrice == 0) {
      return 0;
    }
    ArgumentChecker.isTrue(otmPrice > 0.0, "negative OTM price of {} given", otmPrice);
    ArgumentChecker.isTrue(otmPrice < Math.min(forward, strike), "otmPrice of {} exceeded upper bound of {}", otmPrice, Math.min(forward, strike));
    ArgumentChecker.isTrue(volGuess > 0.0, "negative volGuess");

    if (forward == strike) {
      return NORMAL.getInverseCDF(0.5 * (otmPrice / forward + 1)) * 2 / Math.sqrt(timeToExpiry);
    }

    boolean isCall = strike >= forward;

    double lowerSigma;
    double upperSigma;

    try {
      final double[] temp = bracketRoot(otmPrice, forward, strike, timeToExpiry, isCall, volGuess, Math.min(volGuess, 0.1));
      lowerSigma = temp[0];
      upperSigma = temp[1];
    } catch (final MathException e) {
      throw new IllegalArgumentException(e.toString() + " No implied Volatility for this price. [price: " + otmPrice + ", forward: " + forward + ", strike: " + strike + ", timeToExpiry: "
          + timeToExpiry + ", " + (isCall ? "Call" : "put"));
    }
    double sigma = (lowerSigma + upperSigma) / 2.0;
    final double maxChange = 0.5;

    double[] pnv = priceAndVega(forward, strike, timeToExpiry, sigma, isCall);
    //TODO check if this is ever called
    if (pnv[1] == 0 || Double.isNaN(pnv[1])) {
      return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
    }
    double diff = pnv[0] / otmPrice - 1.0;
    boolean above = diff > 0;
    if (above) {
      upperSigma = sigma;
    } else {
      lowerSigma = sigma;
    }

    double trialChange = -diff * otmPrice / pnv[1];
    double actChange;
    if (trialChange > 0.0) {
      actChange = Math.min(maxChange, Math.min(trialChange, upperSigma - sigma));
    } else {
      actChange = Math.max(-maxChange, Math.max(trialChange, lowerSigma - sigma));
    }

    int count = 0;
    while (Math.abs(actChange) > VOL_TOL) {
      sigma += actChange;
      pnv = priceAndVega(forward, strike, timeToExpiry, sigma, isCall);

      if (pnv[1] == 0 || Double.isNaN(pnv[1])) {
        return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
      }

      diff = pnv[0] / otmPrice - 1.0;
      above = diff > 0;
      if (above) {
        upperSigma = sigma;
      } else {
        lowerSigma = sigma;
      }

      trialChange = -diff * otmPrice / pnv[1];
      if (trialChange > 0.0) {
        actChange = Math.min(maxChange, Math.min(trialChange, upperSigma - sigma));
      } else {
        actChange = Math.max(-maxChange, Math.max(trialChange, lowerSigma - sigma));
      }

      if (count++ > MAX_ITERATIONS) {
        return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
      }
    }
    return sigma;
  }

  public static double impliedVolatility(final SimpleOptionData data, final double price) {
    return impliedVolatility(price / data.getDiscountFactor(), data.getForward(), data.getStrike(), data.getTimeToExpiry(), data.isCall());
  }

  public static double impliedVolatility(final SimpleOptionData[] data, final double price) {
    Validate.notEmpty(data, "no option data given");
    double intrinsicPrice = 0.0;
    for (final SimpleOptionData option : data) {
      intrinsicPrice += Math.max(0, (option.isCall() ? 1 : -1) * option.getDiscountFactor() * (option.getForward() - option.getStrike()));
    }
    Validate.isTrue(price >= intrinsicPrice, "option price (" + price + ") less than intrinsic value (" + intrinsicPrice + ")");

    if (Double.doubleToLongBits(price) == Double.doubleToLongBits(intrinsicPrice)) {
      return 0.0;
    }
    double sigma = 0.3;

    final double maxChange = 0.5;

    double modelPrice = 0.0;
    double vega = 0.0;
    for (final SimpleOptionData option : data) {
      modelPrice += price(option, sigma);
      vega += vega(option, sigma);
    }

    if (vega == 0 || Double.isNaN(vega)) {
      return solveByBisection(data, price, sigma, 0.1);
    }
    double change = (modelPrice - price) / vega;
    double previousChange = 0.0;

    double sign = Math.signum(change);
    change = sign * Math.min(maxChange, Math.abs(change));
    if (change > 0 && change > sigma) {
      change = sigma;
    }
    int count = 0;
    while (Math.abs(change) > VOL_TOL) {
      sigma -= change;

      modelPrice = 0.0;
      vega = 0.0;
      for (final SimpleOptionData option : data) {
        modelPrice += price(option, sigma);
        vega += vega(option, sigma);
      }

      if (vega == 0 || Double.isNaN(vega)) {
        return solveByBisection(data, price, sigma, 0.1);
      }
      change = (modelPrice - price) / vega;
      sign = Math.signum(change);
      change = sign * Math.min(maxChange, Math.abs(change));
      if (change > 0 && change > sigma) {
        change = sigma;
      }

      //detect oscillation around the solution
      if (count > 5 && Math.abs(previousChange + change) < VOL_TOL) {
        change /= 2.0;
      }
      previousChange = change;

      if (count++ > MAX_ITERATIONS) {
        return solveByBisection(data, price, sigma, change);
      }
    }
    return sigma;
  }

  /**
   * Computes the implied strike from delta and volatility in the Black formula.
   * @param delta The option delta
   * @param isCall The call (true) / put (false) flag.
   * @param forward The forward.
   * @param time The time to expiration.
   * @param volatility The volatility.
   * @return The strike.
   */
  @ExternalFunction
  public static double impliedStrike(final double delta, final boolean isCall, final double forward, final double time, final double volatility) {
    Validate.isTrue(delta > -1 && delta < 1, "Delta out of range");
    Validate.isTrue(isCall ^ (delta < 0), "Delta incompatible with call/put: " + isCall + ", " + delta);
    Validate.isTrue(forward > 0, "Forward negative");
    final double omega = (isCall ? 1.0 : -1.0);
    final double strike = forward * Math.exp(-volatility * Math.sqrt(time) * omega * NORMAL.getInverseCDF(omega * delta) + volatility * volatility * time / 2);
    return strike;
  }

  /**
   * Computes the implied strike and its derivatives from delta and volatility in the Black formula.
   * @param delta The option delta
   * @param isCall The call (true) / put (false) flag.
   * @param forward The forward.
   * @param time The time to expiration.
   * @param volatility The volatility.
   * @param derivatives The derivatives of the implied strike with respect to the input. The array is changed by the method.
   * Derivatives with respect to: [0] delta, [1] forward, [2] time, [3] volatility. 
   * @return The strike.
   */
  public static double impliedStrike(final double delta, final boolean isCall, final double forward, final double time, final double volatility, double[] derivatives) {
    Validate.isTrue(delta > -1 && delta < 1, "Delta out of range");
    Validate.isTrue(isCall ^ (delta < 0), "Delta incompatible with call/put: " + isCall + ", " + delta);
    Validate.isTrue(forward > 0, "Forward negative");
    final double omega = (isCall ? 1.0 : -1.0);
    final double sqrtt = Math.sqrt(time);
    final double n = NORMAL.getInverseCDF(omega * delta);
    final double part1 = Math.exp(-volatility * sqrtt * omega * n + volatility * volatility * time / 2);
    final double strike = forward * part1;
    // Backward sweep
    double strikeBar = 1.0;
    double part1Bar = forward * strikeBar;
    double nBar = part1 * -volatility * Math.sqrt(time) * omega * part1Bar;
    derivatives[0] = omega / NORMAL.getPDF(n) * nBar;
    derivatives[1] = part1 * strikeBar;
    derivatives[2] = part1 * (-volatility * omega * n * 0.5 / sqrtt + volatility * volatility / 2) * part1Bar;
    derivatives[3] = part1 * (-sqrtt * omega * n + volatility * time) * part1Bar;
    return strike;
  }

  private static double[] priceAndVega(final double forward, final double strike, final double timeToExpiry, final double lognormalVol, final boolean isCall) {

    final double rootT = Math.sqrt(timeToExpiry);
    final double sigmaRootT = lognormalVol * rootT;
    final double[] res = new double[2];

    if (Math.abs(forward - strike) < SMALL) {
      res[0] = forward * (2 * NORMAL.getCDF(sigmaRootT / 2) - 1);
      res[1] = forward * rootT * NORMAL.getPDF(sigmaRootT / 2);
      return res;
    }

    final int sign = isCall ? 1 : -1;

    if (sigmaRootT < SMALL || strike < SMALL) {
      res[0] = Math.max(sign * (forward - strike), 0.0);
      res[1] = 0.0;
      return res;
    }

    final double d1 = Math.log(forward / strike) / sigmaRootT + 0.5 * sigmaRootT;
    final double d2 = d1 - sigmaRootT;
    res[0] = sign * (forward * NORMAL.getCDF(sign * d1) - strike * NORMAL.getCDF(sign * d2));
    res[1] = forward * rootT * NORMAL.getPDF(d1);
    return res;
  }

  private static double[] bracketRoot(final double forwardPrice, final double forward, final double strike, final double expiry, final boolean isCall, final double sigma, final double change) {
    final BracketRoot bracketer = new BracketRoot();
    final Function1D<Double, Double> func = new Function1D<Double, Double>() {
      @Override
      public Double evaluate(final Double volatility) {
        return price(forward, strike, expiry, volatility, isCall) / forwardPrice - 1.0;
      }
    };
    return bracketer.getBracketedPoints(func, sigma - Math.abs(change), sigma + Math.abs(change), 0, Double.POSITIVE_INFINITY);
  }

  private static double solveByBisection(final double forwardPrice, final double forward, final double strike, final double expiry, final boolean isCall, final double lowerSigma,
      final double upperSigma) {

    final BisectionSingleRootFinder rootFinder = new BisectionSingleRootFinder(VOL_TOL);
    final Function1D<Double, Double> func = new Function1D<Double, Double>() {

      @Override
      public Double evaluate(final Double volatility) {
        double trialPrice = price(forward, strike, expiry, volatility, isCall);
        return trialPrice / forwardPrice - 1.0;
      }
    };
    return rootFinder.getRoot(func, lowerSigma, upperSigma);
  }

  private static double solveByBisection(final SimpleOptionData[] data, final double price, final double sigma, final double change) {
    final BracketRoot bracketer = new BracketRoot();
    final BisectionSingleRootFinder rootFinder = new BisectionSingleRootFinder(EPS);
    final int n = data.length;
    final Function1D<Double, Double> func = new Function1D<Double, Double>() {

      @Override
      public Double evaluate(final Double volatility) {
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
          sum += price(data[i], volatility);
        }
        return sum - price;
      }
    };
    final double[] range = bracketer.getBracketedPoints(func, sigma - Math.abs(change), sigma + Math.abs(change));
    return rootFinder.getRoot(func, range[0], range[1]);
  }

  /**
   * Given marked price and implied volatility, return the forward that is implied  
   * @param otmPrice The <b>forward</b> price - i.e. the market price divided by the numeraire (i.e. the zero bond p(0,T) for the T-forward measure)
   * <b>Note</b> This MUST be an OTM price - i.e. a call price for strike >= forward and a put price otherwise 
   * @param forward The forward value of the underlying
   * @param strike The Strike
   * @param timeToExpiry The time-to-expiry
   * @param volGuess a guess of the implied volatility 
   * @return log-normal (Black) implied volatility
   
  @ExternalFunction
  public static double impliedForward(final double otmPrice, final double impliedVol, final double strike, final double timeToExpiry, final double volGuess) {
    if (otmPrice == 0) {
      return 0;
    }
    ArgumentChecker.isTrue(otmPrice > 0.0, "negative OTM price of {} given", otmPrice);
    ArgumentChecker.isTrue(otmPrice < Math.min(forward, strike), "otmPrice of {} exceeded upper bound of {}", otmPrice, Math.min(forward, strike));
    ArgumentChecker.isTrue(volGuess > 0.0, "negative volGuess");

    if (forward == strike) {
      return NORMAL.getInverseCDF(0.5 * (otmPrice / forward + 1)) * 2 / Math.sqrt(timeToExpiry);
    }

    boolean isCall = strike >= forward;

    double lowerSigma;
    double upperSigma;

    try {
      final double[] temp = bracketRoot(otmPrice, forward, strike, timeToExpiry, isCall, volGuess, Math.min(volGuess, 0.1));
      lowerSigma = temp[0];
      upperSigma = temp[1];
    } catch (final MathException e) {
      throw new IllegalArgumentException(e.toString() + " No implied Volatility for this price. [price: " + otmPrice + ", forward: " + forward + ", strike: " +
          strike + ", timeToExpiry: " + timeToExpiry + ", " + (isCall ? "Call" : "put"));
    }
    double sigma = (lowerSigma + upperSigma) / 2.0;
    final double maxChange = 0.5;

    double[] pnv = priceAndVega(forward, strike, timeToExpiry, sigma, isCall);
    //TODO check if this is ever called
    if (pnv[1] == 0 || Double.isNaN(pnv[1])) {
      return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
    }
    double diff = pnv[0] / otmPrice - 1.0;
    boolean above = diff > 0;
    if (above) {
      upperSigma = sigma;
    } else {
      lowerSigma = sigma;
    }

    double trialChange = -diff * otmPrice / pnv[1];
    double actChange;
    if (trialChange > 0.0) {
      actChange = Math.min(maxChange, Math.min(trialChange, upperSigma - sigma));
    } else {
      actChange = Math.max(-maxChange, Math.max(trialChange, lowerSigma - sigma));
    }

    int count = 0;
    while (Math.abs(actChange) > VOL_TOL) {
      sigma += actChange;
      pnv = priceAndVega(forward, strike, timeToExpiry, sigma, isCall);

      if (pnv[1] == 0 || Double.isNaN(pnv[1])) {
        return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
      }

      diff = pnv[0] / otmPrice - 1.0;
      above = diff > 0;
      if (above) {
        upperSigma = sigma;
      } else {
        lowerSigma = sigma;
      }

      trialChange = -diff * otmPrice / pnv[1];
      if (trialChange > 0.0) {
        actChange = Math.min(maxChange, Math.min(trialChange, upperSigma - sigma));
      } else {
        actChange = Math.max(-maxChange, Math.max(trialChange, lowerSigma - sigma));
      }

      if (count++ > MAX_ITERATIONS) {
        return solveByBisection(otmPrice, forward, strike, timeToExpiry, isCall, lowerSigma, upperSigma);
      }
    }
    return sigma;
  }
  */
}
