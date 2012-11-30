/**
 * Copyright 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 *
 * maps URL hash routes
 */
$.register_module({
    name: 'og.common.routes',
    dependencies: ['og.dev'],
    obj: function () {
        var routes, set_title = function (title) {document.title = 'OpenGamma: ' + title;},
            hash = window.RouteMap.hash;
        return routes = $.extend(true, window.RouteMap, {
            init: function () {
                var title, go = routes.go;
                // overwrite routes.go so it can accept new title parameter
                routes.go = function (location, new_title) {
                    title = new_title;
                    go(location);
                };
                // listen to all clicks that bubble up and capture their titles
                // overwrite href with new filter values
                $('a[href]').live('click', function (e) {
                    var $anchor = $(this), current, parsed, rule, view, href;
                    title = $anchor.attr('title');
                    if (!$anchor.is('.og-js-live-anchor')) return;
                    view = og.views[(parsed = routes.parse($anchor.attr('href'))).page.slice(1)];
                    if (!view.filter_params.length || (current = routes.current()).page !== parsed.page) return;
                    rule = parsed.args.id ? 'load_item' : 'load';
                    href = routes.prefix() + routes.hash(view.rules[rule], parsed.args, {
                        add: view.filter_params
                            .reduce(function (acc, val) {return (acc[val] = current.args[val]), acc;}, {})
                    });
                    $anchor.attr('href', href);
                });
                $(window).on('hashchange', function () {
                    routes.handler();
                    set_title(title || (new Date).toLocaleTimeString());
                    title = null;
                });
                // escape key will break long-polling, so prevent the default action
                $(window).on('keydown', function (e) {if (e.keyCode === $.ui.keyCode.ESCAPE) e.preventDefault();});
                $(function () { // in addition to binding hash change events to window, also fire it onload
                    var common = og.views.common, is_child, parent_api, api = og.api.rest;
                    $('.OG-js-loading').hide();
                    $('.OG-layout-admin-container, .OG-layout-analytics-container, .OG-layout-blotter-container')
                        .css({'visibility': 'visible'});
                    common.layout = (({
                        'analytics.ftl': common.layout.analytics,
                        'analytics2.ftl': common.layout.analytics2,
                        'blotter.ftl': common.layout.blotter,
                        'gadget.ftl': common.layout.gadget,
                        'admin.ftl': common.layout.admin
                    })[window.location.pathname.split('/').reverse()[0].toLowerCase()] || $.noop)();
                    // check if the parent's document is the same as the window's (instead of just comparing
                    // window.parent to window, we use document because IE8 doesn't know true from false)
                    is_child = (window.parent.document !== window.document) || window.opener;
                    parent_api = (window.opener && window.opener.og && window.opener.og.api.rest) ||
                        window.parent.og.api.rest;
                    if (is_child && parent_api) og.api.rest = parent_api; else if (og.api.rest) api.subscribe();
                    routes.handler();
                    set_title((new Date).toLocaleTimeString());
                });
                // IE does not allow deleting from window so set to void 0 if it fails
                try {delete window.RouteMap;} catch (error) {window.RouteMap = void 0;}
            },
            hash: function (rule, params, extras) {
                var modified_params;
                if (!extras) return hash(rule, params);
                modified_params = $.extend({}, params);
                if (extras.add) $.extend(modified_params, extras.add);
                if (extras.del) extras.del.forEach(function (param) {delete modified_params[param];});
                return hash(rule, modified_params);
            },
            post_add: function (compiled) { // add optional debug param to all rules that don't ask for it
                if (!~compiled.rules.keyvals.pluck('name').indexOf('debug'))
                    compiled.rules.keyvals.push({name: 'debug', required: false});
                return compiled;
            },
            pre_dispatch: function (parsed) { // if the debug param exists, use it to set API debug mode
                if (parsed.length && parsed[0].args.debug) og.dev.debug = parsed[0].args.debug === 'true';
                if (og.api.rest) og.api.rest.clean();
                return parsed;
            }
        });
    }
});