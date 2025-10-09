import axios from "axios";

// 请求接口
export const request = (
    url,
    method,
    data,
    headers = {},
) => {
    return new Promise((resolve, reject) => {
        const mergedHeaders = {
            // 如果是 POST 请求，默认添加 'Content-Type'
            ...(method?.toUpperCase() === "POST" ? { 'Content-Type': 'application/json' } : {}),
            ...headers  // 用户自定义 headers 覆盖默认
        }
        axios({
            url,
            // 默认为GET请求
            method: method || "GET",
            // 不存在headers时，默认为'Content-Type': 'application/json',
            headers: mergedHeaders,
            // GET请求参数在url中
            params: method.toUpperCase() === "GET" ? data : {},
            // POST请求参数在data中
            data: method.toUpperCase() === "POST" ? data : {},
        })
            .then((res) => {
                resolve(res.data);
            })
            .catch((err) => {
                reject(err);
            });
    });
}