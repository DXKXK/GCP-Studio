export function dealyData(data, ms = 1000) {
    return new Promise(resolve => {
        setInterval(() => {
            resolve(data);
        }, ms)
    })
}