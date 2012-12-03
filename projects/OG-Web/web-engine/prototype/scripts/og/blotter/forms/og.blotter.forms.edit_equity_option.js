/**
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.blotter.forms.Edit_equity_option',
    dependencies: [],
    obj: function () {   
        return function () {
            var constructor = this;
            constructor.load = function () {
                constructor.title = 'Equity Option Assignment/Termination';
                var form = new og.common.util.ui.Form({
                    module: 'og.blotter.forms.edit_equity_option_tash',
                    data: {},
                    type_map: {},
                    selector: '.OG-blotter-form-block',
                    extras:{}
                });
                form.children.push(
                    new form.Block({
                        module: 'og.blotter.forms.blocks.edit_action_tash',
                        extras: {}//needs partial term
                    }),
                    new form.Block({
                        module: 'og.blotter.forms.blocks.edit_assignment_tash',
                        extras: {}
                    })
                );
                form.dom();
            }; 
            constructor.load();
            constructor.kill = function () {
            };
        };
    }
});