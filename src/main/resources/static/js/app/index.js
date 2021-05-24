var main = {
    init : function() {
        var _this = this;
        $('#btn-car-save').on('click', function() {
            _this.carSave();
        });

        $('#btn-car-update').on('click', function() {
            _this.carUpdate();
        });

        $('#btn-car-delete').on('click', function() {
            _this.carDelete();
        });
    },

    changeCategory : function(category) {
        if (category == '국산') {
            return 'DOMESTIC';
        } else {
            return 'FOREIGN';
        }
    },

    validateCar : function() {
        var result = true;
        if ($('#staff').val() == '') {
            $('#staff').addClass('fieldError');
            result = false;
        } else {
            $('#staff').removeClass('fieldError');
        }

        if ($('#color').val() == '') {
            $('#color').addClass('fieldError');
            result = false;
        } else {
            $('#color').removeClass('fieldError');
        }

        if ($('#model').val() == '') {
            $('#model').addClass('fieldError');
            result = false;
        } else {
            $('#model').removeClass('fieldError');
        }

        if ($('#vin').val() == '') {
            $('#vin').addClass('fieldError');
            result = false;
        } else {
            $('#vin').removeClass('fieldError');
        }

        if ($('#carNumber').val() == '') {
            $('#carNumber').addClass('fieldError');
            result = false;
        } else {
            $('#carNumber').removeClass('fieldError');
        }

        if (!result) {
            alert('입력되지 않은 값이 있습니다.');
        }
        return result;
    },

    carSave : function() {
        var _this = this;
        if (!_this.validateCar()) {
            return;
        }

        var data = {
            carNumber: $('#carNumber').val(),
            vin: $('#vin').val(),
            category: _this.changeCategory($('#category').val()),
            model: $('#model').val(),
            color: $('#color').val(),
            productionYear: $('#productionYear').val(),
            staff: $('#staff').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/car',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('차량이 매입되었습니다.');
            window.location.href = '/car/findAll';
        }).fail(function(error) {
            alert(error.responseJSON.message);
        });
    },

    carUpdate : function() {
        var _this = this;
        if (!_this.validateCar()) {
            return;
        }

        var data = {
            category: _this.changeCategory($('#category').val()),
            model: $('#model').val(),
            color: $('#color').val(),
            productionYear: $('#productionYear').val(),
            staff: $('#staff').val()
        };

        $.ajax({
            type: 'PUT',
            url: '/api/car/' + $('#id').val(),
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('차량 정보가 수정되었습니다.');
            window.location.href = '/car/findAll';
        }).fail(function(error) {
            alert(error.responseJSON.message);
        });
    },

    carDelete : function() {
            var _this = this;

            $.ajax({
                type: 'DELETE',
                url: '/api/car/' + $('#id').val(),
                dataType: 'json',
                contentType:'application/json; charset=utf-8'
            }).done(function() {
                alert('차량이 삭제되었습니다.');
                window.location.href = '/car/findAll';
            }).fail(function(error) {
                alert(error.responseJSON.message);
            });
        }
};

main.init();