/**
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.common.gadgets.data',
    dependencies: ['og.common.gadgets.manager'],
    obj: function () {
        var prefix = 'og_data_gadget_', counter = 1;
        return function (config) {
            // 2 col
            config.data = {
                data: [
                    [0.02459016393442623, 0.0018060011461625827],
                    [0.04371584699453552, 0.002076640695228338],
                    [0.09016393442622951, 0.0023631408766479653],
                    [0.1721311475409836, 0.0033244274036651348],
                    [0.26229508196721313, 0.004353994402860873],
                    [0.5195074481622876, 0.0043019573857417046],
                    [0.7633430646006437, 0.004269684155888322],
                    [1.0099184070663971, 0.0042958688377172285],
                    [2.0099184070663974, 0.004846199774741556],
                    [3.007178681039, 0.005913726961415006],
                    [4.005464480874317, 0.007668908279876522],
                    [5.004438955011603, 0.009939502948991086],
                    [6.004438955011603, 0.012132388956713308],
                    [7.009918407066397, 0.014284336398286994],
                    [8.008196721311474, 0.016159939544590284],
                    [9.004438955011603, 0.01770050621711515],
                    [10.004438955011603, 0.019283215802868743],
                    [15.004438955011603, 0.02399867741581842],
                    [20.005464480874316, 0.025831020950183217],
                    [25.007178681039, 0.02675796697842828],
                    [30.009918407066397, 0.027182958134956005]
                ],
                labels: ['Maturity', 'Percentage']
            };
            // 2 col
            config.data2 = {
                data: [
                    [0.02459016393442623, 0.0018060011461625827],
                    [0.04371584699453552, 0.002076640695228338],
                    [0.09016393442622951, 0.0023631408766479653],
                    [0.1721311475409836, 0.0033244274036651348],
                    [0.26229508196721313, 0.004353994402860873],
                    [0.5195074481622876, 0.0043019573857417046],
                    [0.7633430646006437, 0.004269684155888322],
                    [1.0099184070663971, 0.0042958688377172285],
                    [2.0099184070663974, 0.004846199774741556],
                    [3.007178681039, 0.005913726961415006],
                    [4.005464480874317, 0.007668908279876522],
                    [5.004438955011603, 0.009939502948991086],
                    [6.004438955011603, 0.012132388956713308],
                    [7.009918407066397, 0.014284336398286994],
                    [8.008196721311474, 0.016159939544590284],
                    [9.004438955011603, 0.01770050621711515],
                    [10.004438955011603, 0.019283215802868743],
                    [15.004438955011603, 0.02399867741581842],
                    [20.005464480874316, 0.025831020950183217],
                    [25.007178681039, 0.02675796697842828],
                    [30.009918407066397, 0.027182958134956005]
                ],
                labels: ['Something', 'Else']
            };
            // 3 col
            config.data3 = { /* 3 */
                data: [
                    [0.02459016393442623, 0.0018060011461625827, 0.002076640695228338],
                    [0.04371584699453552, 0.002076640695228338, 0.1721311475409836],
                    [0.09016393442622951, 0.0023631408766479653, 0.1721311475409836],
                    [0.1721311475409836, 0.0033244274036651348, 0.1721311475409836],
                    [0.26229508196721313, 0.004353994402860873, 0.5195074481622876],
                    [0.5195074481622876, 0.0043019573857417046, 0.5195074481622876],
                    [0.7633430646006437, 0.004269684155888322, 0.5195074481622876],
                    [1.0099184070663971, 0.0042958688377172285, 0.5195074481622876],
                    [2.0099184070663974, 0.004846199774741556, 3.007178681039],
                    [3.007178681039, 0.005913726961415006, 3.007178681039],
                    [4.005464480874317, 0.007668908279876522, 3.007178681039],
                    [5.004438955011603, 0.009939502948991086, 3.007178681039],
                    [6.004438955011603, 0.012132388956713308, 3.007178681039],
                    [7.009918407066397, 0.014284336398286994, 3.007178681039],
                    [8.008196721311474, 0.016159939544590284, 3.007178681039],
                    [9.004438955011603, 0.01770050621711515, 3.007178681039],
                    [10.004438955011603, 0.019283215802868743, 3.007178681039],
                    [15.004438955011603, 0.02399867741581842, 3.007178681039],
                    [20.005464480874316, 0.025831020950183217, 3.007178681039],
                    [25.007178681039, 0.02675796697842828, 3.007178681039],
                    [30.009918407066397, 0.027182958134956005, 3.007178681039]
                ],
                labels: ['Something', 'Else', 'three']
            };
            // matrix
            config.data4 = {
                data: [
                    [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 373846.7048838872],
                    [12.115561763375597, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
                    [0.0, 3393.2390681685065, 0.0, 0.0, 193469.00834880827, 0.0, 0.0, 0.0],
                    [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 333803.55959295307, 0.0],
                    [0.0, 0.0, 0.0, 0.0, 0.0, 270964.2925522737, 0.0, 0.0],
                    [0.0, 0.0, 48233.38798109766, 124334.85478654894, 0.0, 0.0, 0.0, 0.0],
                    [-58.0951756315859, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0],
                    [0.0, - 14689.329368831579, - 190388.656581803, - 472504.39904162334, 0.0, 0.0, 0.0, 0.0],
                    [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, - 1072112.8999647116, 0.0],
                    [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, - 1133727.213932],
                    [0.0, 0.0, 0.0, 0.0, 0.0, - 932468.1725114007, 0.0, 0.0],
                    [0.0, 0.0, 0.0, 0.0, - 708294.9437590293, 0.0, 0.0, 0.0]
                ],
                labels : [
                    [
                        "0.31693989071038253", "0.8154652294333409", "1.3168350924470396", "1.8154652294333409",
                        "2.3168350924470396", "2.815465229433341", "3.314095366419642", "3.8114754098360657"
                    ],
                    [
                        "0.9985403099034356", "0.9999101729171346", "1.0", "1.0000823414926265", "1.0014447189160864",
                        "1.0054794520547945", "4.999910172917135", "5.0", "5.000082341492627", "5.001280035930833",
                        "5.001444718916087", "5.0027397260273965"
                    ]
                ]
            };
            var gadget = this, data, alive = prefix + counter++;
            gadget.alive = function () {return !!$('.' + alive).length;};
            gadget.load = function () {
                $(config.selector)
                    .addClass(alive)
                    .css({position: 'absolute', top: '0', left: 0, right: 0, bottom: 0, borderTop: '1px solid #fff'});
                data = $(config.selector).ogdata([config.data4]);
            };
            gadget.resize = gadget.load;
            gadget.load();
            if (!config.child) og.common.gadgets.manager.register(gadget);
        };
    }
});