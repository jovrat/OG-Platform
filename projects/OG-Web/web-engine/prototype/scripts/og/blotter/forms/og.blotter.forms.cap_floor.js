/**
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.blotter.forms.Cap_floor',
    dependencies: [],
    obj: function () {   
        return function () {
            var constructor = this;
            constructor.load = function () {
                constructor.title = 'Cap/Floor';
                var form = new og.common.util.ui.Form({
                    module: 'og.blotter.forms.cap_floor_tash',
                    data: {},
                    type_map: {},
                    selector: '.OG-blotter-form-block',
                    extras:{}
                });
                form.children.push(
                    new form.Block({
                        module: 'og.blotter.forms.blocks.cap_floor_tash',
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