(function ($) {
    var sep = '_';
    var defaults = {
        div: '<div class="dropdown bts_dropdown"></div>',
        buttontext: '- 선택 -',
        button: '<button class="custom-select text-left" type="button" data-toggle="dropdown"><span></span></button>',
        ul: '<ul class="dropdown-menu multiselect-container"></ul>',
        li: '<li><label></label></li>'
    };

    $.fn.treeselect = function (options) {
        var $select = $(this);
        var id = $select.attr('id');

        var settings = $.extend(defaults, options);

        var $div = $(settings.div);
        var $button = $(settings.button);
        var $ul = $(settings.ul).click(function (e) {
            e.stopPropagation();
        });

        initialize();

        function initialize() {
            $select.after($div);
            $div.append($button);
            $div.append($ul);

            createList();
            updateButtonText();

            $select.remove();
            $div.attr('id', id);
            $div.data('buttontext', settings.buttontext);
        }

        function createStructure(selector) {
            var options = [];

            $select.children(selector).each(function (i, el) {
                $el = $(el);

                options.push({
                    value: $el.val(),
                    text: $el.text(),
                    checked: $el.attr('selected') ? true : false,
                    disabled: $el.attr('disabled') ? true : false, // custom
                    level: $el.data('level'), // custom
                    children: createStructure('option[data-parent=' + $el.val() + ']'),
                    opt: el
                });
            });

            return options;
        }

        function createListItem(option) {
            var $li = $(settings.li);
            $label = $li.children('label');
            $label.text(option.text);

            if ($select.attr('multiple')) {
                // custom
                var parent = $(option.opt).data('parent') ? $(option.opt).data('parent') : '0';
                $input = $('<input type="checkbox" name="' + $select.attr('name').replace('[]','') + '" value="' + option.value + '" data-parent="' + parent + '" data-level="' + option.level + '">');
                if (option.disabled) {
                    $input.attr('disabled', 'disabled');
                    $li.addClass('disabled');
                }
            } else {
                $input = $('<input type="radio" name="' + $select.attr('name') +'" value="' + option.value + '">');
            }

            if (option.checked)
                $input.attr('checked', 'checked');
            $label.prepend($input);

            $input.on('click', function () {
                // custom
                if ($(this).attr('type') == 'checkbox') {
                    updateChecked(this);
                }
                updateButtonText();
            });

            $input.on('click', function () {
                updateButtonText();
            });

            if (option.children.length > 0 && !option.disabled) {
                $li.addClass('multiselect-group closed');
                var $toggle = $('<span class="caret-container dropdown-toggle"></span>');
                $li.prepend($toggle);
                $toggle.on('click', function() {
                    if ($li.hasClass('closed')) {
                        showItem($li);
                    } else {
                        hideItem($li);
                    }
                });

                $(option.children).each(function (i, child) {
                    $childul = $('<ul style="display:none"></ul>').appendTo($li);
                    $childul.append(createListItem(child));
                });
            } else {
                var $toggle = $('<span class="caret-container"></span>');
                $li.prepend($toggle);
            }

            return $li;
        }

        function createList() {
            $(createStructure('option:not([data-parent])')).each(function (i, option) {
                $li = createListItem(option);
                $ul.append($li);
            });
        }

        function updateButtonText() {
            buttontext = [];

            $div.find('input').each(function (i, el) {
                $checkbox = $(el);
                //if ($checkbox.is(':checked') && $('input[value^="' + $checkbox.val() + sep + '"]').length < 1) { // custom
                if ($checkbox.is(':checked') && $('input[data-parent="' + $checkbox.val() + '"]').length < 1) { // custom
                    buttontext.push($checkbox.parent().text());
                }
            });

            if (buttontext.length > 0) {
                if (buttontext.length < 4) {
                    $button.children('span').text(buttontext.join(', '));
                } else if ($div.find('input').length == buttontext.length) {
                    $button.children('span').text('전체선택 (' + buttontext.length + ')');
                } else {
                    $button.children('span').text(buttontext.length + '개 선택');
                }
            } else {
                $button.children('span').text(settings.buttontext);
            }
        }

        // custom
        function updateChecked(el) {
            var $el = $(el);

            // 하위 제어
            //$('input[value^="' + $el.val() + sep + '"]').not(':disabled').prop('checked', $el.prop('checked'));
            $('input[data-parent="' + $el.val() + '"]').each(function() {
                $(this).not(':disabled').prop('checked', $el.prop('checked'));
                if ($('input[data-parent="' + $(this).val() + '"]').length > 0) {
                    $('input[data-parent="' + $(this).val() + '"]').not(':disabled').prop('checked', $el.prop('checked'));
                }
            });

            // 상위 제어
            var inputs = $('input[name="' + $el.attr('name') + '"]');
            //inputs.sort(function(a, b) {
            //    return $(b).val().length - $(a).val().length;
            //});

            $(inputs).each(function(i, input) {
                var parent = $(input).data('parent');
                if ( $('input[data-parent="' + parent + '"]').not(':disabled').not(':checked').length < 1 ) {
                    $('input[data-code="' + parent + '"]').not(':disabled').prop('checked', true);
                } else {
                    $('input[data-code="' + parent + '"]').prop('checked', false);
                }
            });
        }
    };
}(jQuery));

function hideItem($li) {
    var $childul = $li.children('ul');
    $childul.hide(100);
    $li.addClass('closed');
}

function showItem($li) {
    var $childul = $li.children('ul');
    $childul.show(100);
    $li.removeClass('closed');
}
