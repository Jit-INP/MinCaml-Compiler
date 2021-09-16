let rec f _ = 1 in
let rec g _ = 2 in
let rec h _ = 3 in

let x = f () in
let y = g () in
print_int ((if h () = 0 then x - y else y - x) + x + y)
