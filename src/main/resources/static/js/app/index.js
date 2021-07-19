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

        $('#btn-release').on('click', function() {
            _this.carRelease();
        });

        $('#btn-release-update').on('click', function() {
            _this.releaseStatusUpdate();
        });

        $('#btn-car-search').on('click', function() {
            _this.carSearch();
        });

        $('#btn-car-normal-search').on('click', function() {
            _this.carNormalSearch();
        });

        $('#btn-release-search').on('click', function() {
            _this.releaseSearch();
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
            console.log(error);
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
            console.log(error);
            alert(error.responseJSON.message);
            window.location.href = '/car/findAll';
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
            console.log(error);
            alert(error.responseJSON.message);
            window.location.href = '/car/findAll';
        });
    },

    carRelease : function() {
        var _this = this;
        var payments = [];
        var cashPayment = {
            paymentType: 'CASH',
            pay_amount: $('#cash_pay_amount').val().replace(/,/g,'')
        }

        var cardPayment = {
            paymentType: 'CARD',
            pay_amount: $('#card_pay_amount').val().replace(/,/g,''),
            instalment: $('#instalment').val(),
            capital: $('#capital').val()
        }

        payments.push(cashPayment);
        payments.push(cardPayment);

        var data = {
            staff: $('#staff').val(),
            salesStaff: $('#salesStaff').val(),
            price: $('#price').val().replace(/,/g,''),
            carId: $('#carId').val(),
            status: 'READY',
            payments: payments
        };

        $.ajax({
            type: 'POST',
            url: '/api/release',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('차량이 출고되었습니다.');
            window.location.href = '/release/findAll';
        }).fail(function(error) {
            console.log(error);
            alert(error.responseJSON.message);
        });
    },

    releaseStatusUpdate : function() {
        var _this = this;

        var status = $('#status').val();
        if (status == '입금 대기') {
            status = 'READY';
        } else if (status == '출고 완료') {
            status = 'COMPLETE';
        } else {
            status = 'CANCEL';
        }
        var data = {
            status: status
        };

        $.ajax({
            type: 'PUT',
            url: '/api/release/' + $('#releaseId').val(),
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('출고 상태가 변경되었습니다.');
            window.location.href = '/release/findAll';
        }).fail(function(error) {
            console.log(error);
            alert(error.responseJSON.message);
            window.location.href = '/release/findAll';
        });
    },

    carSearch : function() {
        var _this = this;

        var data = {
            model: $('#car-search-model').val(),
            status: $('#car-search-status').val()
        };

        $.ajax({
            type: 'GET',
            url: '/api/car/search',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: data
        }).done(function(data) {
            createCarList(data.data);
        }).fail(function(error) {
            console.log(error);
            alert(error.responseJSON.message);
        });
    },

    carNormalSearch : function() {
        var _this = this;

        var data = {
            model: $('#car-search-model').val(),
            status: 'NORMAL'
        };

        $.ajax({
            type: 'GET',
            url: '/api/car/search',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: data
        }).done(function(data) {
            createCarNormalList(data.data);
        }).fail(function(error) {
            console.log(error);
            alert(error.responseJSON.message);
        });
    },

    releaseSearch : function() {
        var _this = this;

        var data = {
            model: $('#release-search-model').val(),
            status: $('#release-search-status').val(),
            startDate: $('#release-search-startDate').val(),
            endDate: $('#release-search-endDate').val(),
        };

        $.ajax({
            type: 'GET',
            url: '/api/release/search',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: data
        }).done(function(data) {
            createReleaseList(data.data);
        }).fail(function(error) {
            console.log(error);
            alert(error.responseJSON.message);
        });
    }
};

main.init();


function numberWithCommas(x) {
    var xVal= $("#" + x).val();
    xVal = xVal.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    xVal = xVal.replace(/,/g,'');          // ,값 공백처리
    $("#" + x).val(xVal.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
}

function calculateReleaseAmount(x) {
    var price = $('#price').val();
    if (price == null || price == '') {
        alert('판매 가격이 입력되지 않았습니다.');
        $('#cash_pay_amount').val('');
        $('#card_pay_amount').val('');
        return;
    }

    var xVal= $("#" + x).val();
    xVal = xVal.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    xVal = xVal.replace(/,/g,'');          // ,값 공백처리
    $("#" + x).val(xVal.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가

    price = parseInt($('#price').val().replace(/,/g,''));

    var cash_pay_amount = parseInt($('#cash_pay_amount').val().replace(/,/g,''));
    if (cash_pay_amount > price) {
        alert('판매가격보다 큰 금액을 입력할 수 없습니다.');
        $('#cash_pay_amount').val('');
        $('#card_pay_amount').val('');
        return;
    }

    var card_pay_amount = String(price - cash_pay_amount);
    card_pay_amount = card_pay_amount.replace(/[^0-9]/g,'');   // 입력값이 숫자가 아니면 공백
    card_pay_amount = card_pay_amount.replace(/,/g,'');          // ,값 공백처리
    $('#card_pay_amount').val(card_pay_amount.replace(/\B(?=(\d{3})+(?!\d))/g, ",")); // 정규식을 이용해서 3자리 마다 , 추가
}

function normalRelease(id, carNumber, model, color) {
    var form = $("<form>");
    form.attr("method", "GET");
    form.attr("action", "/release");

    var idTag = $("<input type='hidden' name='carId'>");
    idTag.val(id);

    var carNumberTag = $("<input type='hidden' name='carNumber'>");
    carNumberTag.val(carNumber);

    var modelTag = $("<input type='hidden' name='carModel'>");
    modelTag.val(model);

    var colorTag = $("<input type='hidden' name='color'>");
    colorTag.val(color);
    form.append(idTag);
    form.append(carNumberTag);
    form.append(modelTag);
    form.append(colorTag);

    $('#car-find-normal-body').append(form);
    form.submit();
}

function createCarList(data) {
    var tBody = $('#car-find-all-tbody');
    tBody.children().remove();

    $.each(data, function(index, el) {
        var tr = $('<tr>');

        var tdId = $('<td>');
        tdId.text(el.id);
        tr.append(tdId);

        var tdModel = $('<td>')
        var aModel = $('<a href="' + '/car/find/' + el.id + '">');
        aModel.text(el.model);
        tdModel.append(aModel);
        tr.append(tdModel);

        var tdColor = $('<td>');
        tdColor.text(el.color);
        tr.append(tdColor);

        var tdProductionYear = $('<td>');
        tdProductionYear.text(el.productionYear);
        tr.append(tdProductionYear);

        var tdStaff = $('<td>');
        tdStaff.text(el.staff);
        tr.append(tdStaff);

        var tdStatus = null;
        if (el.released) {
            tdStatus = $('<td class="font-red">');
        } else {
            tdStatus = $('<td class="font-green">');
        }
        tdStatus.text(el.status);
        tr.append(tdStatus);

        tBody.append(tr);
    });
}

function createCarNormalList(data) {
    var tBody = $('#car-find-normal-tbody');
    tBody.children().remove();

    $.each(data, function(index, el) {
        var tr = $('<tr>');

        var tdId = $('<td>');
        tdId.text(el.id);
        tr.append(tdId);

        var tdModel = $('<td>')
        tdModel.text(el.model);
        tr.append(tdModel);

        var tdColor = $('<td>');
        tdColor.text(el.color);
        tr.append(tdColor);

        var tdProductionYear = $('<td>');
        tdProductionYear.text(el.productionYear);
        tr.append(tdProductionYear);

        var tdStaff = $('<td>');
        tdStaff.text(el.staff);
        tr.append(tdStaff);

        var tdButton = $('<td>');
        var aButton = $('<a href="javascript:void(0);" class="btn btn-success" onclick="normalRelease(' +
           "'" + el.id + "', '" + el.carNumber + "', '" + el.model + "', '" + el.color + "');" + '">');
        aButton.text('출고');
        tdButton.append(aButton);
        tr.append(tdButton);

        tBody.append(tr);
    });
}

function createReleaseList(data) {
    var tBody = $('#release-find-all-tbody');
    tBody.children().remove();

    $.each(data, function(index, el) {
        var tr = $('<tr>');

        var tdId = $('<td>');
        tdId.text(el.id);
        tr.append(tdId);

        var tdModel = $('<td>')
        var aModel = $('<a href="' + '/release/find/' + el.id + '">');
        aModel.text(el.car.model);
        tdModel.append(aModel);
        tr.append(tdModel);

        var tdColor = $('<td>');
        tdColor.text(el.car.color);
        tr.append(tdColor);

        var tdCarNumber = $('<td>');
        tdCarNumber.text(el.car.carNumber);
        tr.append(tdCarNumber);

        var tdReleaseDate = $('<td>');
        tdReleaseDate.text(el.formattedReleaseDate);
        tr.append(tdReleaseDate);

        var tdStatus = null;
        if (el.statusReady) {
            tdStatus = $('<td class="font-green">');
        } else if (el.statusComplete) {
            tdStatus = $('<td class="font-blue">');
        } else if (el.statusCancel) {
            tdStatus = $('<td class="font-red">');
        }
        tdStatus.text(el.statusString);
        tr.append(tdStatus);

        tBody.append(tr);
    });
}