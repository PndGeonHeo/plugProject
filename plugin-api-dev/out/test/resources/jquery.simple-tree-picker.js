/*! $.simpleTreePicker JS
 @version 1.1.0
 @author Alex Meub
 @license WTFPL http://www.wtfpl.net/about/
*/

;(function ( $, window, document, undefined ) {

    var pluginName = "treepicker",
    // The Name of Using in .data()
        dataPlugin = "plugin_" + pluginName,
    // Default Options
        defaults = {
            tree: {},
            onclick: null,
            name: 'tree-' + String(Math.floor(Math.random() * 100) + 1),
            inputname: 'checkbox',
            code: 'code',
            codename: 'name'
        };

    // The actual plugin constructor
    var Plugin = function ( element ) {
        this.options = $.extend( {}, defaults );
    };

    Plugin.prototype = {

        init: function ( options ) {

            // Extend Options
            $.extend( this.options, options );
            var data = this.options["tree"],
                onclick = this.options["onclick"],
                selected = this.options["selected"],
                elementName = this.options["name"],
                inputname = this.options["inputname"],
                code = this.options['code'],
                codename = this.options['codename'],
                parentcode = this.options['parentcode'],
                buttonid = this.options['buttonid'],
                buttontext = this.options['buttontext'],
                el = this.element;
            // Build the UL Tree
            // Need to refactor, duplicate code in buildSubTree

            var buildTree = function(data) {
                var html = '<div class="simple-tree '+ elementName +'">',
                    result;

                if (Array.isArray(data)) {
                    $(data).each(function() {
                        html += buildSubTree([this], html, 0);
                    });
                } else {
                    html += buildSubTree([data], html, 0);
                }
                html += "</div>";
                result = $(html);

                updateButtonText();

                // All click functionality in here
                result.on('click', function(event) {

                    var tagName = event.target.nodeName,
                        classes = event.target.classList;

                    // Show/hide subtrees
                    if (tagName == "SPAN" && $.inArray("no-caret", classes) == -1) {
                        var $span = $(event.target).closest('span'),
                            $subtree = $span.parent().children("ul");
                        if ($subtree.is(":visible")) {
                            $span.addClass("closed");
                            $subtree.hide(100);
                        } else {
                            $span.removeClass("closed");
                            $subtree.show(100);
                        }
                    }
                    // Handle changing tree state
                    else if (tagName == "INPUT" || tagName == "LABEL") {

                        // Find actual clicked element
                        var $checkbox = $(event.target).closest('input[type="checkbox"]'),

                        // Find Subtree on Same Level as clicked Input
                            $container = $(event.target).closest('li'),
                            checked = $checkbox.prop("checked");

                        checkChildren($container, checked);
                        checkParent($container, checked);

                        if( onclick ){
                            onclick();
                        }
                    }
                    // Don't prevent default, we want default checking functionality
                });

                result.on('change', function(event) {
                    if (event.target.nodeName == "INPUT") {
//                    console.log('change');
//                        if (result.find(':checkbox:checked').length == result.find(':checkbox').length) {
//                            result.closest('.dropdown-menu').find('.checkbox-all').prop('checked', true);
//
//                        } else {
//                            result.closest('.dropdown-menu').find('.checkbox-all').prop('checked', false);
//                        }
                        updateButtonText();
                    }
                });

                return result;
            };

            // Build Subtree Recursively
            var buildSubTree = function(obj, html, i) {
                if (!obj || !obj.length) return "";
                var t = '';
                if (i == 0) {
                    t = '<ul class="subtree">';
                } else {
                    t = '<ul class="subtree" style="display:none;">';
                }
                $(obj).each(function(j) {
                    if (i == 0) {
                        t += '<li class="tree-node">';
                    } else {
                        t += '<li class="tree-node hirk-node">';
                    }
                    //t += '<div class="node-area">';
                    if (this.children && this.children.length) t += '<span class="caret-container dropdown-toggle closed"></span>';
                    else t += '<span class="caret-container dropdown-toggle no-caret"></span>';
                    t += '<input type="checkbox" id="'+inputname + '-' + this[code] +'" name="' + inputname + '" value="' + this[code] + '" data-code="' + this[code] + '" data-name="' + this[codename] + '" data-depth="' + this.depth + '" data-is-last="' + this.is_last + '" data-parent="' + this[parentcode] + '">';
                    t += '<label for="'+inputname + '-' + this[code] +'">' + this[codename] + '</label>';
                    //t += '</div>';
                    i++;
                    t += buildSubTree(this.children, t, i);
                    t += "</li>";
                });
                t += '</ul>';
				
                return t;
            };

            var updateButtonText = function() {
                var $button = $('#' + buttonid);
                if ($button.length) {
                    var checkCnt = $(el).find('input[name="' + inputname + '"][data-is-last="true"]:checked').length;
                    if (checkCnt < 1) {
                        $button.text(buttontext);
                    } else {
                        $button.text(checkCnt + '개 선택');
                    }
                }
            };

//            var checkAll =  '<div class="top">';
//                checkAll += '    <input type="checkbox" class="checkbox-all" id="' + inputname + '-checkbox-all">';
//                checkAll += '    <label for="' + inputname + '-checkbox-all">전체선택</label>';
//                checkAll += '</div>';
//            $(el).append(checkAll);
//            $(el).find('.checkbox-all').on('click', function(e) {
//                    console.log('all change');
//                var allchecked = $(this).prop('checked');
//                $(this).closest('.dropdown-menu').find(':checkbox').prop('checked', allchecked);
//                updateButtonText();
//            });

            $(el).append(buildTree(data));
            $(el).addClass('treepicker');

            // Go through "selected" locations and select them
            if( selected && selected.length ){
               for( var i =0; i< selected.length; i++ ) {
                  var $checkbox = $(el).find('input[type="checkbox"][data-code="' + selected[i] + '"]'),
                      $container = $checkbox.closest('li');

                  $checkbox.prop("checked", true);
                  checkChildren($container, true);
                  checkParent($container, true);
               }
            }

            $(el).click(function(e) {
                e.stopPropagation();
            });
        },
        // Public Clear
        clear: function () {

            $(this.element).find('input').each(function(){
                var $checkbox = $(this);
                $checkbox.prop({
                    indeterminate: false,
                    checked: false
                });
                $checkbox.change();
            });
            return true;
        },
        // Public Getter (get checked value array)
        // Should refactor these both into one
        val: function () {
            var $checked = $(this.element).find('input[type="checkbox"][data-is-last="true"]:checked');
            return Array.from($checked.map((i, e) => $(e).val()));
        },
        // Public Display (get checked text array)
        display: function () {
            var $checked = $(this.element).find('input[type="checkbox"][data-is-last="true"]:checked');
            return Array.from($checked.map((i, e) => $(e).parent().text()));
        },
        // Public Setter
        set: function (valuesArray) {

            this.clear();
            var el = this.element;
            var inputname = this.options["inputname"];

            $(valuesArray).each(function(){
                var $checkbox = $(el).find('input[type="checkbox"][data-code="' + this + '"]');
                if( $checkbox ) {
					$container = $checkbox.closest('li');
					$checkbox.prop("checked", true)
					checkChildren($container, true);
					checkParent($container, true);
					$checkbox.change();
				}
				else
                    return false;
            });

            return true;
        },
        destroy: function () {
            $(this.element).empty();
            return true;
        }
    };

    // Check Children Recursively
    var checkChildren = function(li, checked) {
        if (!li) return;
        var $li = $(li),
            $ul = $li.children("ul.subtree").length && $li.children("ul.subtree"),
            $checkbox = $li.children('input[type="checkbox"]');
        if ($ul) {
            $ul.children("li").each(function() {
                var childli = $(this);
                var hasChildUl = !! childli.children("ul.subtree").length;
                var childCheckbox = childli.children('input[type="checkbox"]');
                childCheckbox.prop("checked", checked);
                // If Parent state is known, child cannot be indeterminate
                childCheckbox[0].indeterminate = false;
                if (hasChildUl) checkChildren(this, checked);
            });
        } else {
            $checkbox.prop("checked", checked);
        }
    };

    // Check Parent Recursively
    var checkParent = function(li, checked) {
        if (!li) return;
        var $parent = $(li).closest(".subtree"),
            $checkbox = $parent.siblings("input"),
            checkedChildren = $parent.children("li").children("input:checkbox:checked"),
            uncheckedChildren = $parent.children("li").children("input:checkbox:not(:checked)"),
            indeterminateChildren = false; // sad, sad children ;(

        // Find any indeterminate children
        $parent.children("li").children("input:checkbox:not(:checked)").each( function(){
            if( this.indeterminate ){
                indeterminateChildren = true;
            }
        });
        // Set up conditions
        var noSelectedChildren = !checkedChildren.length,
            someSelectedChildren = checkedChildren.length && uncheckedChildren.length,
            allSelectedChildren = !uncheckedChildren.length;

        // If parent has no selected or indeterminate children, its unchecked
        if( noSelectedChildren && !indeterminateChildren){
            $checkbox.prop({
                indeterminate: false,
                checked: false
            });
        }
        // If parent has all selected children, it's checked
        else if( allSelectedChildren ){
            $checkbox.prop({
                indeterminate: false,
                checked: "checked"
            });
        }
        // If parent has some unselected children, or indeterminate children, its indeterminate
        else if( someSelectedChildren || indeterminateChildren){
            $checkbox.prop({
                indeterminate: true,
                checked: false
            });
        }
        if( $parent.parent().length )
            checkParent( $parent.parent(), checked );
    };

    /*
     * Plugin wrapper, preventing against multiple instantiations and
     * allowing any public function to be called via the jQuery plugin,
     * e.g. $(element).pluginName('functionName', arg1, arg2, ...)
     */
    $.fn[ pluginName ] = function ( arg ) {

        var args, instance;

        // only allow the plugin to be instantiated once
        if (!( this.data( dataPlugin ) instanceof Plugin )) {

            // if no instance, create one
            this.data( dataPlugin, new Plugin( this ) );
        }

        instance = this.data( dataPlugin );
        instance.element = this;

        // Is the first parameter an object (arg), or was omitted,
        // call Plugin.init( arg )
        if ( typeof arg === 'undefined' || typeof arg === 'object' ) {

            if ( typeof instance['init'] === 'function' ) {
                instance.init( arg );
            }
            // checks that the requested public method exists
        } else if ( typeof arg === 'string' && typeof instance[arg] === 'function' ) {
            // copy arguments & remove function name
            args = Array.prototype.slice.call( arguments, 1 );
            // call the method
            return instance[arg].apply( instance, args );

        } else {
            $.error( 'Method ' + arg + ' does not exist on jQuery.' + pluginName );
        }
    };

}(jQuery, window, document));

