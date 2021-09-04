const url = './';

function loadCurrencyList() {
    $.ajax({
        url: url + '/currencies',
        method: 'GET',
        complete: function (data) {
            let codesList = JSON.parse(data.responseText);
            let select = document.querySelector("#currency_select");
            select.innerHTML = '';
            for (let i = 0; i < codesList.length; i++) {
                let option = document.createElement("option");
                option.value = codesList[i];
                option.text = codesList[i];
                select.insertAdjacentElement("beforeend", option);
            }
        }
    })
}

function loadGif() {
    let code = $("#currency_select").val();
    $.ajax({
        url: url + 'gif?code=' + code,
        method: 'GET',
        dataType: "json",
        complete: function (data) {
            let content = JSON.parse(data.responseText);
            let img = document.createElement("img");
            img.src = content.data.images.original.url;

            let gifName = document.createElement("p");
            gifName.textContent = content.data.title;

            let tag = document.createElement("t");
            tag.textContent = content.tag;

            let gif = document.querySelector("#gif");
            gif.innerHTML = '';
            gif.insertAdjacentElement("afterbegin", img);
            gif.insertAdjacentElement("afterbegin", gifName);
            gif.insertAdjacentElement("afterbegin", tag);
        }
    })
}
