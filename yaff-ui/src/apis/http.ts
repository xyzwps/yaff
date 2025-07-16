import ky, { type Options, type SearchParamsOption } from "ky";

export type HttpOptions = {
  url: string;
  qs?: SearchParamsOption;
  json?: unknown;
};

const toOptions = (opts: HttpOptions): Options => {
  return {
    searchParams: opts.qs,
    json: opts.json,
  };
};

export function get<T>(opts: HttpOptions) {
  return ky.get(opts.url, toOptions(opts)).json<T>();
}

export function post<T>(opts: HttpOptions) {
  return ky.post(opts.url, toOptions(opts)).json<T>();
}

export function put<T>(opts: HttpOptions) {
  return ky.put(opts.url, toOptions(opts)).json<T>();
}

export function del<T>(opts: HttpOptions) {
  return ky.delete(opts.url, toOptions(opts)).json<T>();
}
