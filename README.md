# Ledger v0.7
Ledger is a small program that I initially designed and created in the summer of 2018. 
In early 2018, I became heavily interested in cryptocurrencies, and as I explored them further, I was frustrated in not having a "central ledger" in which I could check the balances of all my coins as well as the current net worth of all my coins.

Thus I created Ledger in an attempt to organize my coins and check their total worth in one single place.
Users can manually enter the amount of a few select coins they have into the program, and the program will look at an API and calculate the total worth of each quantity of coin, as well as a grand total. 
The totals dynamically update as balances are changed, and upon closing the program, the balances are saved and will be loaded up upon the next run.

In 2020 I revisted Ledger, and made a great deal of improvements and optimizations since 2018's v0.6
-Switched API calls from the CoinMarketCap to CoinGecko
-API calls are much more efficient, now only one call is made for all the coins, whereas before one call was made for each coin
-Completely redone coin-list system, which moved from pre-created classes and objects for each coin to a single class and dynamically created objects for each coin

In the future I'll need to
-Find a better solution/workaround to handling thumbnails for the coins as coins are unfortunately still "hardcoded" 
-Add a button to dynamically add additional coins
-Optimize the updating of the balances/totals during runtime

New in v0.7 (26 May 2020) - API calls switched to CoinGecko. Only a single API call is made per run. Optimized coin-list system. Many more comments and documentation.
New in v0.6 (24 Nov 2018) - API calls updated to CMC Pro-API. There are still 7 different API calls, which CMC is now discouraging by setting a daily call limit. Future versions will need to consist of only a single API call.
