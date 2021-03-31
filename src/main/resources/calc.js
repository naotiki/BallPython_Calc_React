//Web Worker使うためだけに移植
// Dart->Kotlin->JavaScript
self.addEventListener('message', function (e) {


    let obj = JSON.parse(e.data);
    console.log(obj);
    let male = obj.first;//Array
    let female = obj.second;// "
    let pairs = [ /*[[0,0],[0,0]]*/];
    male.forEach(function (element) {
        let i = getIndex(female, element)
        console.log(i)
        if (i !== -1) {
            pairs.push([[element.first, element.second], [female[i].first, female[i].second]]);
            female.splice(i, 1);
        } else {
            pairs.push([[element.first, element.second], [0, 0]]);
        }
    });
    female.forEach(function (element) {
        pairs.push([[element.first, element.second], [0, 0]]);
    });
    if (pairs.length !== 1) {
        pairs = pairs.filter(it => it[0] !== [0,0]);
    }
    console.log(pairs)
    let all = Math.pow(4, pairs.length);
    let children = [];
    for (let i = 0; i < all; i++) {
        children.push([])
    }

    for (let i = 0; i < pairs.length; i++) {
        let pair = pairs[i]
        let groups = [
            [pair[0][0], pair[1][0]],
            [pair[0][0], pair[1][1]],
            [pair[0][1], pair[1][0]],
            [pair[0][1], pair[1][1]],
        ];
        let groupIndex = 0;
        let index = 0;
        while (index < all) {
            for (let j = 0; j < all / Math.pow(4, i + 1); j++) {


                children[index].push(groups[groupIndex])
                index++
            }
            groupIndex++
            if (groupIndex >= 4) groupIndex = 0
        }


    }
    console.log(children)
    //処理結果を送信
    self.postMessage(JSON.stringify(children));
}, false);

function getIndex(array, value) {
    for (let i = 0; i < array.length; i++) {
        if (array[i].first === value.first) {

            return i
        }
    }
    return -1

}
