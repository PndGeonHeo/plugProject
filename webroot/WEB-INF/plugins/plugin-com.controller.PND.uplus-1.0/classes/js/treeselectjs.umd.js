var Hi = Object.defineProperty;
var Gi = (b, p, f) => p in b ? Hi(b, p, {
    enumerable: !0,
    configurable: !0,
    writable: !0,
    value: f
}) : b[p] = f;
var c = (b, p, f) => (Gi(b, typeof p != "symbol" ? p + "" : p, f), f),
    yt = (b, p, f) => {
        if (!p.has(b)) throw TypeError("Cannot " + f)
    };
var n = (b, p, f) => (yt(b, p, "read from private field"), f ? f.call(b) : p.get(b)),
    r = (b, p, f) => {
        if (p.has(b)) throw TypeError("Cannot add the same private member more than once");
        p instanceof WeakSet ? p.add(b) : p.set(b, f)
    },
    C = (b, p, f, U) => (yt(b, p, "write to private field"), U ? U.call(b, f) : p.set(b, f), f);
var o = (b, p, f) => (yt(b, p, "access private method"), f);
(function(b, p) {
    typeof exports == "object" && typeof module < "u" ? module.exports = p() : typeof define == "function" && define.amd ? define(p) : (b = typeof globalThis < "u" ? globalThis : b || self, b.Treeselect = p())
})(this, function() {
    var P, y, H, x, pe, Ut, G, oe, me, zt, fe, Yt, M, re, B, j, be, Kt, Ce, Xt, ge, Jt, ke, Zt, we, Qt, Ee, es, ve, ts, Le, ss, ye, is, xe, ls, Se, ns, _e, as, Ae, os, Te, rs, Ne, cs, Oe, hs, z, xt, F, V, _, Y, Ie, ds, Pe, us, Be, ps, Ve, ms, De, fs, He, bs, K, St, Ge, Cs, Me, gs, Fe, ks, X, _t, je, ws, qe, Es, Re, vs, $e, Ls, We, ys, Ue, xs, ze, Ss, Ye, _s, Ke, As, Xe, Ts, Je, Ns, J, At, Z, Tt, Ze, Os, u, m, q, Q, R, A, T, S, D, ee, Nt, te, Ot, Qe, Is, et, Ps, tt, Bs, st, Vs, it, Ds, lt, Hs, se, It, nt, Gs, at, Ms, ot, Fs, rt, js, ie, Pt, ct, qs, $, gt, le, Bt, W, kt, ht, Rs, ne, Vt, dt, $s, ut, Ws, pt, Us, mt, zs, ft, Ys;
    "use strict";
    const b = "",
        p = {
            arrowUp: '<svg class="arrowUp" xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 15l-6-6-6 6"/></svg>',
            arrowDown: '<svg class="arrowDown" xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 9l6 6 6-6"/></svg>',
            arrowRight: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 18l6-6-6-6"/></svg>',
            attention: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>',
            clear: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="15" y1="9" x2="9" y2="15"></line><line x1="9" y1="9" x2="15" y2="15"></line></svg>',
            cross: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>',
            check: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"></polyline></svg>',
            partialCheck: '<svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 25 25" fill="none" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line></svg>'
        },
        f = (l, e) => {
            if (e.innerHTML = "", typeof l == "string") e.innerHTML = l;
            else {
                const t = l.cloneNode(!0);
                e.appendChild(t)
            }
        },
        U = l => {
            const e = l ? {
                ...l
            } : {};
            return Object.keys(p).forEach(t => {
                e[t] || (e[t] = p[t])
            }), e
        },
        Ks = l => l.reduce((e, {
            name: t
        }, s) => (e += t, s < l.length - 1 && (e += ", "), e), "");
    class Xs {
        constructor({
            value: e,
            showTags: t,
            tagsCountText: s,
            clearable: i,
            isAlwaysOpened: a,
            searchable: h,
            placeholder: d,
            disabled: w,
            isSingleSelect: g,
            id: k,
            ariaLabel: E,
            iconElements: v,
            inputCallback: L,
            searchCallback: N,
            openCallback: O,
            closeCallback: ae,
            keydownCallback: bt,
            focusCallback: vt,
            blurCallback: Lt,
            nameChangeCallback: Ct
        }) {
            r(this, pe);
            r(this, G);
            r(this, me);
            r(this, fe);
            r(this, M);
            r(this, B);
            r(this, be);
            r(this, Ce);
            r(this, ge);
            r(this, ke);
            r(this, we);
            r(this, Ee);
            r(this, ve);
            r(this, Le);
            r(this, ye);
            r(this, xe);
            r(this, Se);
            r(this, _e);
            r(this, Ae);
            r(this, Te);
            r(this, Ne);
            r(this, Oe);
            r(this, z);
            c(this, "value");
            c(this, "showTags");
            c(this, "tagsCountText");
            c(this, "clearable");
            c(this, "isAlwaysOpened");
            c(this, "searchable");
            c(this, "placeholder");
            c(this, "disabled");
            c(this, "isSingleSelect");
            c(this, "id");
            c(this, "ariaLabel");
            c(this, "iconElements");
            c(this, "isOpened");
            c(this, "searchText");
            c(this, "srcElement");
            r(this, P, void 0);
            r(this, y, void 0);
            r(this, H, void 0);
            r(this, x, void 0);
            c(this, "inputCallback");
            c(this, "searchCallback");
            c(this, "openCallback");
            c(this, "closeCallback");
            c(this, "keydownCallback");
            c(this, "focusCallback");
            c(this, "blurCallback");
            c(this, "nameChangeCallback");
            this.value = e, this.showTags = t, this.tagsCountText = s, this.searchable = h, this.placeholder = d, this.clearable = i, this.isAlwaysOpened = a, this.disabled = w, this.isSingleSelect = g, this.id = k, this.ariaLabel = E, this.iconElements = v, this.isOpened = !1, this.searchText = "", C(this, P, o(this, ge, Jt).call(this)), C(this, y, o(this, ye, is).call(this)), C(this, H, o(this, _e, as).call(this)), C(this, x, null), this.inputCallback = L, this.searchCallback = N, this.openCallback = O, this.closeCallback = ae, this.keydownCallback = bt, this.focusCallback = vt, this.blurCallback = Lt, this.nameChangeCallback = Ct, this.srcElement = o(this, be, Kt).call(this, n(this, P), n(this, y), n(this, H)), o(this, pe, Ut).call(this)
        }
        focus() {
            setTimeout(() => n(this, y).focus(), 0)
        }
        blur() {
            this.isOpened && o(this, B, j).call(this), this.clearSearch(), n(this, y).blur()
        }
        updateValue(e) {
            this.value = e, o(this, G, oe).call(this), o(this, M, re).call(this)
        }
        removeItem(e) {
            this.value = this.value.filter(t => t.id !== e), o(this, z, xt).call(this), o(this, G, oe).call(this), o(this, M, re).call(this)
        }
        clear() {
            this.value = [], o(this, z, xt).call(this), o(this, G, oe).call(this), this.clearSearch()
        }
        openClose() {
            o(this, B, j).call(this)
        }
        clearSearch() {
            this.searchText = "", this.searchCallback(""), o(this, M, re).call(this)
        }
    }
    P = new WeakMap, y = new WeakMap, H = new WeakMap, x = new WeakMap, pe = new WeakSet, Ut = function() {
        o(this, G, oe).call(this), o(this, M, re).call(this), o(this, me, zt).call(this)
    }, G = new WeakSet, oe = function() {
        if (n(this, P).innerHTML = "", this.showTags) {
            n(this, P).append(...o(this, ke, Zt).call(this));
            const e = Ks(this.value);
            this.nameChangeCallback(e)
        } else {
            const e = o(this, Le, ss).call(this);
            n(this, P).appendChild(e), this.nameChangeCallback(e.innerText, this.value)
        }
        n(this, P).appendChild(n(this, y))
    }, me = new WeakSet, zt = function() {
        const e = [];
        n(this, H).innerHTML = "", this.clearable && e.push(o(this, Ae, os).call(this)), this.isAlwaysOpened || e.push(o(this, Ne, cs).call(this, this.isOpened)), e.length && n(this, H).append(...e)
    }, fe = new WeakSet, Yt = function() {
        if (!this.isAlwaysOpened && n(this, x)) {
            const e = this.isOpened ? this.iconElements.arrowUp : this.iconElements.arrowDown;
            f(e, n(this, x))
        }
    }, M = new WeakSet, re = function() {
        var e;
        (e = this.value) != null && e.length ? (n(this, y).removeAttribute("placeholder"), this.srcElement.classList.remove("treeselect-input--value-not-selected")) : (n(this, y).setAttribute("placeholder", this.placeholder), this.srcElement.classList.add("treeselect-input--value-not-selected")), this.searchable ? this.srcElement.classList.remove("treeselect-input--unsearchable") : this.srcElement.classList.add("treeselect-input--unsearchable"), this.isSingleSelect ? this.srcElement.classList.add("treeselect-input--is-single-select") : this.srcElement.classList.remove("treeselect-input--is-single-select"), n(this, y).value = this.searchText
    }, B = new WeakSet, j = function() {
        this.isOpened = !this.isOpened, o(this, fe, Yt).call(this), this.isOpened ? this.openCallback() : this.closeCallback()
    }, be = new WeakSet, Kt = function(e, t, s) {
        const i = document.createElement("div");
        return i.classList.add("treeselect-input"), i.setAttribute("tabindex", "-1"), i.addEventListener("mousedown", a => o(this, Ce, Xt).call(this, a)), i.addEventListener("focus", () => this.focusCallback(), !0), i.addEventListener("blur", () => this.blurCallback(), !0), e.appendChild(t), i.append(e, s), i
    }, Ce = new WeakSet, Xt = function(e) {
        e.stopPropagation(), this.isOpened || o(this, B, j).call(this), this.focus()
    }, ge = new WeakSet, Jt = function() {
        const e = document.createElement("div");
        this.clearable ? e.style.width = 'calc(100% - 40px)' : e.style.width = 'calc(100% - 20px)';
        return e.classList.add("treeselect-input__tags"), e
    }, ke = new WeakSet, Zt = function() {
        return this.value.map(e => {
            const t = document.createElement("div");
            t.classList.add("treeselect-input__tags-element"), t.setAttribute("tabindex", "-1"), t.setAttribute("tag-id", e.id.toString()), t.setAttribute("title", e.name);
            const s = o(this, Ee, es).call(this, e.name),
                i = o(this, ve, ts).call(this);
            return t.addEventListener("mousedown", a => o(this, we, Qt).call(this, a, e.id)), t.append(s, i), t
        })
    }, we = new WeakSet, Qt = function(e, t) {
        e.preventDefault(), e.stopPropagation(), this.removeItem(t), this.focus()
    }, Ee = new WeakSet, es = function(e) {
        const t = document.createElement("span");
        return t.classList.add("treeselect-input__tags-name"), t.textContent = e, t
    }, ve = new WeakSet, ts = function() {
        const e = document.createElement("span");
        return e.classList.add("treeselect-input__tags-cross"), f(this.iconElements.cross, e), e
    }, Le = new WeakSet, ss = function() {
        const e = document.createElement("span");
        if (e.classList.add("treeselect-input__tags-count"), !this.value.length) return e.textContent = "", e.setAttribute("title", ""), e;
        const t = this.value.length === 1 ? this.value[0].name : `${this.value.length} ${this.tagsCountText}`;
        return e.textContent = t, e.setAttribute("title", t), e
    }, ye = new WeakSet, is = function() {
        const e = document.createElement("input");
        return e.classList.add("treeselect-input__edit"), this.id && e.setAttribute("id", this.id), (!this.searchable || this.disabled) && e.setAttribute("readonly", "readonly"), this.disabled && e.setAttribute("tabindex", "-1"), this.ariaLabel.length && e.setAttribute("aria-label", this.ariaLabel), e.addEventListener("keydown", t => o(this, xe, ls).call(this, t)), e.addEventListener("input", t => o(this, Se, ns).call(this, t, e)), e
    }, xe = new WeakSet, ls = function(e) {
        e.stopPropagation();
        const t = e.key;
        t === "Backspace" && !this.searchText.length && this.value.length && !this.showTags && this.clear(), t === "Backspace" && !this.searchText.length && this.value.length && this.removeItem(this.value[this.value.length - 1].id), e.code === "Space" && (!this.searchText || !this.searchable) && o(this, B, j).call(this), (t === "Enter" || t === "ArrowDown" || t === "ArrowUp") && e.preventDefault(), this.keydownCallback(e), t !== "Tab" && this.focus()
    }, Se = new WeakSet, ns = function(e, t) {
        e.stopPropagation();
        const s = this.searchText,
            i = t.value.trim();
        if (s.length === 0 && i.length === 0) {
            t.value = "";
            return
        }
        if (this.searchable) {
            const a = e.target.value;
            this.searchCallback(a), this.isOpened || o(this, B, j).call(this)
        } else t.value = "";
        this.searchText = t.value
    }, _e = new WeakSet, as = function() {
        const e = document.createElement("div");
        return e.classList.add("treeselect-input__operators"), e
    }, Ae = new WeakSet, os = function() {
        const e = document.createElement("span");
        return e.classList.add("treeselect-input__clear"), e.setAttribute("tabindex", "-1"), f(this.iconElements.clear, e), e.addEventListener("mousedown", t => o(this, Te, rs).call(this, t)), e
    }, Te = new WeakSet, rs = function(e) {
        e.preventDefault(), e.stopPropagation(), (this.searchText.length || this.value.length) && this.clear(), this.focus()
    }, Ne = new WeakSet, cs = function(e) {
        C(this, x, document.createElement("span")), n(this, x).classList.add("treeselect-input__arrow");
        const t = e ? this.iconElements.arrowUp : this.iconElements.arrowDown;
        return f(t, n(this, x)), n(this, x).addEventListener("mousedown", s => o(this, Oe, hs).call(this, s)), n(this, x)
    }, Oe = new WeakSet, hs = function(e) {
        e.stopPropagation(), e.preventDefault(), this.focus(), o(this, B, j).call(this)
    }, z = new WeakSet, xt = function() {
        this.inputCallback(this.value)
    };
    const Dt = (l, e, t, s) => {
            ei(e);
            const i = e.filter(a => !a.disabled && l.some(h => h === a.id));
            if (t && i.length) {
                i[0].checked = !0;
                return
            }
            i.forEach(a => {
                a.checked = !0;
                const h = wt(a, e, s);
                a.checked = h
            })
        },
        wt = ({
            id: l,
            checked: e
        }, t, s) => {
            const i = t.find(h => h.id === l);
            if (!i) return !1;
            if (s) return i.checked = i.disabled ? !1 : !!e, i.checked;
            const a = Ht(!!e, i, t);
            return Gt(i, t), a
        },
        Ht = (l, e, t) => {
            if (!e.isGroup) return e.checked = e.disabled ? !1 : !!l, e.isPartialChecked = !1, e.checked;
            const s = t.filter(d => d.childOf === e.id);
            return !l || e.disabled || e.isPartialChecked ? (e.checked = !1, e.isPartialChecked = !1, Et(e, s, t), e.checked) : Mt(s, t) ? Ft(s) ? (e.checked = !1, e.isPartialChecked = !1, e.disabled = !0, e.checked) : (e.checked = !1, e.isPartialChecked = !0, s.forEach(d => {
                Ht(l, d, t)
            }), e.checked) : (e.checked = !0, e.isPartialChecked = !1, Et(e, s, t), e.checked)
        },
        Gt = (l, e) => {
            const t = e.find(s => s.id === l.childOf);
            t && (Js(t, e), Gt(t, e))
        },
        Js = (l, e) => {
            const t = ce(l, e);
            if (Ft(t)) {
                l.checked = !1, l.isPartialChecked = !1, l.disabled = !0;
                return
            }
            if (Zs(t)) {
                l.checked = !0, l.isPartialChecked = !1;
                return
            }
            if (Qs(t)) {
                l.checked = !1, l.isPartialChecked = !0;
                return
            }
            l.checked = !1, l.isPartialChecked = !1
        },
        Et = ({
            checked: l,
            disabled: e
        }, t, s) => {
            t.forEach(i => {
                i.disabled = !!e || !!i.disabled, i.checked = !!l && !i.disabled, i.isPartialChecked = !1;
                const a = ce(i, s);
                Et({
                    checked: l,
                    disabled: e
                }, a, s)
            })
        },
        Mt = (l, e) => l.some(i => i.disabled) ? !0 : l.some(i => {
            if (i.isGroup) {
                const a = ce(i, e);
                return Mt(a, e)
            }
            return !1
        }),
        Ft = l => l.every(e => !!e.disabled),
        Zs = l => l.every(e => !!e.checked),
        Qs = l => l.some(e => !!e.checked || !!e.isPartialChecked),
        ei = l => {
            l.forEach(e => {
                e.checked = !1, e.isPartialChecked = !1
            })
        },
        ti = (l, e, t) => {
            const s = {
                    level: 0,
                    groupId: ""
                },
                i = jt(l, e, s.groupId, s.level);
            return ii(i, t)
        },
        jt = (l, e, t, s) => l.reduce((i, a) => {
            var g;
            const h = !!((g = a.children) != null && g.length),
                d = s >= e && h,
                w = s > e;
            if (i.push({
                    id: a.value,
                    name: a.name,
                    childOf: t,
                    isGroup: h,
                    checked: !1,
                    isPartialChecked: !1,
                    level: s,
                    isClosed: d,
                    hidden: w,
                    disabled: a.disabled ?? !1
                }), h) {
                const k = jt(a.children, e, a.value, s + 1);
                i.push(...k)
            }
            return i
        }, []),
        ce = ({
            id: l
        }, e) => e.filter(t => t.childOf === l),
        si = l => {
            const {
                ungroupedNodes: e,
                allGroupedNodes: t,
                allNodes: s
            } = l.reduce((a, h) => (h.checked && (a.allNodes.push(h), h.isGroup ? a.allGroupedNodes.push(h) : a.ungroupedNodes.push(h)), a), {
                ungroupedNodes: [],
                allGroupedNodes: [],
                allNodes: []
            }), i = s.filter(a => !t.some(({
                id: h
            }) => h === a.childOf));
            return {
                ungroupedNodes: e,
                groupedNodes: i,
                allNodes: s
            }
        },
        ii = (l, e) => (l.filter(s => !!s.disabled).forEach(({
            id: s
        }) => wt({
            id: s,
            checked: !1
        }, l, e)), l),
        he = (l, {
            id: e,
            isClosed: t
        }) => {
            ce({
                id: e
            }, l).forEach(i => {
                i.hidden = t ?? !1, i.isGroup && !i.isClosed && he(l, {
                    id: i.id,
                    isClosed: t
                })
            })
        },
        li = l => {
            l.filter(e => e.isGroup && !e.disabled && (e.checked || e.isPartialChecked)).forEach(e => {
                e.isClosed = !1, he(l, e)
            })
        },
        ni = (l, e) => {
            const t = ai(l, e);
            l.forEach(s => {
                t.some(({
                    id: a
                }) => a === s.id) ? (s.isGroup && (s.isClosed = !1, he(l, s)), s.hidden = !1) : s.hidden = !0
            });
        },
        ai = (l, e) => l.reduce((t, s) => {
            if (s.name.toLowerCase().includes(e.toLowerCase())) {
                if (t.push(s), s.isGroup) {
                    const a = qt(s.id, l);
                    t.push(...a)
                }
                if (s.childOf) {
                    const a = Rt(s.childOf, l);
                    t.push(...a)
                }
            }
            return t
        }, []),
        qt = (l, e) => e.reduce((t, s) => (s.childOf === l && (t.push(s), s.isGroup && t.push(...qt(s.id, e))), t), []),
        Rt = (l, e) => e.reduce((t, s) => (s.id === l && (t.push(s), s.childOf && t.push(...Rt(s.childOf, e))), t), []),
        oi = l => {
            const {
                duplications: e
            } = l.reduce((t, s) => (t.allItems.some(i => i.toString() === s.id.toString()) && t.duplications.push(s.id), t.allItems.push(s.id), t), {
                duplications: [],
                allItems: []
            });
            e.length && console.error(`Validation: You have duplicated values: ${e.join(", ")}! You should use unique values.`)
        },
        ri = (l, e, t, s, i, a, h, d, w, g) => {
            Dt(l, e, i, w), d && h && li(e), de(e, t, s, a, g)
        },
        de = (l, e, t, s, i) => {
            l.forEach(a => {
                const h = e.querySelector(`[input-id="${a.id}"]`),
                    d = I(h);
                h.checked = a.checked, ci(a, d, s), hi(a, d), di(a, d), ui(a, d, t), pi(a, d), fi(a, d, l, i), mi(a, h, t)
            }), bi(l, e)
        },
        ci = (l, e, t) => {
            l.checked ? e.classList.add("treeselect-list__item--checked") : e.classList.remove("treeselect-list__item--checked"), Array.isArray(t) && t[0] === l.id && !l.disabled ? e.classList.add("treeselect-list__item--single-selected") : e.classList.remove("treeselect-list__item--single-selected")
        },
        hi = (l, e) => {
            l.isPartialChecked ? e.classList.add("treeselect-list__item--partial-checked") : e.classList.remove("treeselect-list__item--partial-checked")
        },
        di = (l, e) => {
            l.disabled ? e.classList.add("treeselect-list__item--disabled") : e.classList.remove("treeselect-list__item--disabled")
        },
        ui = (l, e, t) => {
            if (l.isGroup) {
                const s = e.querySelector(".treeselect-list__item-icon"),
                    i = l.isClosed ? t.arrowRight : t.arrowDown;
                f(i, s), l.isClosed ? e.classList.add("treeselect-list__item--closed") : e.classList.remove("treeselect-list__item--closed")
            }
        },
        pi = (l, e) => {
            l.hidden ? e.classList.add("treeselect-list__item--hidden") : e.classList.remove("treeselect-list__item--hidden")
        },
        mi = (l, e, t) => {
            const i = e.parentNode.querySelector(".treeselect-list__item-checkbox-icon");
            l.checked ? f(t.check, i) : l.isPartialChecked ? f(t.partialCheck, i) : i.innerHTML = ""
        },
        fi = (l, e, t, s) => {
            const i = l.level === 0,
                a = 20,
                h = 5;
            if (i) {
                const d = t.some(k => k.isGroup && k.level === l.level),
                    w = !l.isGroup && d ? `${a}px` : `${h}px`,
                    g = l.isGroup ? "0" : w;
                s ? e.style.paddingRight = g : e.style.paddingLeft = g
            } else {
                const d = l.isGroup ? `${l.level*a}px` : `${l.level*a+a}px`;
                s ? e.style.paddingRight = d : e.style.paddingLeft = d
            }
            e.setAttribute("level", l.level.toString()), e.setAttribute("group", l.isGroup.toString())
        },
        bi = (l, e) => {
            const t = l.some(i => !i.hidden),
                s = e.querySelector(".treeselect-list__empty");
            t ? s.classList.add("treeselect-list__empty--hidden") : s.classList.remove("treeselect-list__empty--hidden")
        },
        I = l => l.parentNode.parentNode,
        $t = (l, e) => e.find(t => t.id.toString() === l),
        Ci = l => I(l).querySelector(".treeselect-list__item-icon"),
        gi = (l, e) => {
            e && Object.keys(e).forEach(t => {
                const s = e[t];
                typeof s == "string" && l.setAttribute(t, s)
            })
        };
    class ki {
        constructor({
            options: e,
            value: t,
            openLevel: s,
            listSlotHtmlComponent: i,
            emptyText: a,
            isSingleSelect: h,
            iconElements: d,
            showCount: w,
            disabledBranchNode: g,
            expandSelected: k,
            isIndependentNodes: E,
            rtl: v,
            inputCallback: L,
            arrowClickCallback: N,
            mouseupCallback: O
        }) {
            r(this, Ie);
            r(this, Pe);
            r(this, Be);
            r(this, Ve);
            r(this, De);
            r(this, He);
            r(this, K);
            r(this, Ge);
            r(this, Me);
            r(this, Fe);
            r(this, X);
            r(this, je);
            r(this, qe);
            r(this, Re);
            r(this, $e);
            r(this, We);
            r(this, Ue);
            r(this, ze);
            r(this, Ye);
            r(this, Ke);
            r(this, Xe);
            r(this, Je);
            r(this, J);
            r(this, Z);
            r(this, Ze);
            c(this, "options");
            c(this, "value");
            c(this, "openLevel");
            c(this, "listSlotHtmlComponent");
            c(this, "emptyText");
            c(this, "isSingleSelect");
            c(this, "showCount");
            c(this, "disabledBranchNode");
            c(this, "expandSelected");
            c(this, "isIndependentNodes");
            c(this, "rtl");
            c(this, "iconElements");
            c(this, "searchText");
            c(this, "flattedOptions");
            c(this, "flattedOptionsBeforeSearch");
            c(this, "selectedNodes");
            c(this, "srcElement");
            c(this, "inputCallback");
            c(this, "arrowClickCallback");
            c(this, "mouseupCallback");
            r(this, F, null);
            r(this, V, !0);
            r(this, _, []);
            r(this, Y, !0);
            this.options = e, this.value = t, this.openLevel = s ?? 0, this.listSlotHtmlComponent = i ?? null, this.emptyText = a ?? "No results found...", this.isSingleSelect = h ?? !1, this.showCount = w ?? !1, this.disabledBranchNode = g ?? !1, this.expandSelected = k ?? !1, this.isIndependentNodes = E ?? !1, this.rtl = v ?? !1, this.iconElements = d, this.searchText = "", this.flattedOptions = ti(this.options, this.openLevel, this.isIndependentNodes), this.flattedOptionsBeforeSearch = this.flattedOptions, this.selectedNodes = {
                nodes: [],
                groupedNodes: [],
                allNodes: []
            }, this.srcElement = o(this, Be, ps).call(this), this.inputCallback = L, this.arrowClickCallback = N, this.mouseupCallback = O, oi(this.flattedOptions)
        }
        updateValue(e) {
            this.value = e, C(this, _, this.isSingleSelect ? this.value : []), ri(e, this.flattedOptions, this.srcElement, this.iconElements, this.isSingleSelect, n(this, _), this.expandSelected, n(this, Y), this.isIndependentNodes, this.rtl), C(this, Y, !1), o(this, Z, Tt).call(this)
        }
        updateSearchValue(e) {
            if (e === this.searchText) return;
            const t = this.searchText === "" && e !== "";
            this.searchText = e, t && (this.flattedOptionsBeforeSearch = JSON.parse(JSON.stringify(this.flattedOptions))), this.searchText === "" && (this.flattedOptions = this.flattedOptionsBeforeSearch.map(s => {
                const i = this.flattedOptions.find(a => a.id === s.id);
                return i.isClosed = s.isClosed, i.hidden = s.hidden, i
            }), this.flattedOptionsBeforeSearch = []), this.searchText && ni(this.flattedOptions, e), de(this.flattedOptions, this.srcElement, this.iconElements, n(this, _), this.rtl), this.focusFirstListElement()
        }
        callKeyAction(e) {
            C(this, V, !1);
            const t = this.srcElement.querySelector(".treeselect-list__item--focused");
            if (t == null ? void 0 : t.classList.contains("treeselect-list__item--hidden")) return;
            const i = e.key;
            i === "Enter" && t && t.dispatchEvent(new Event("mousedown")), (i === "ArrowLeft" || i === "ArrowRight") && o(this, Ie, ds).call(this, t, e), (i === "ArrowDown" || i === "ArrowUp") && o(this, Pe, us).call(this, t, i)
        }
        focusFirstListElement() {
            const e = "treeselect-list__item--focused",
                t = this.srcElement.querySelector(`.${e}`),
                s = Array.from(this.srcElement.querySelectorAll(".treeselect-list__item-checkbox")).filter(a => window.getComputedStyle(I(a)).display !== "none");
            if (!s.length) return;
            t && t.classList.remove(e), I(s[0]).classList.add(e)
        }
        isLastFocusedElementExist() {
            return !!n(this, F)
        }
    }
    F = new WeakMap, V = new WeakMap, _ = new WeakMap, Y = new WeakMap, Ie = new WeakSet, ds = function(e, t) {
        if (!e) return;
        const s = t.key,
            a = e.querySelector(".treeselect-list__item-checkbox").getAttribute("input-id"),
            h = $t(a, this.flattedOptions),
            d = e.querySelector(".treeselect-list__item-icon");
        s === "ArrowLeft" && !h.isClosed && h.isGroup && (d.dispatchEvent(new Event("mousedown")), t.preventDefault()), s === "ArrowRight" && h.isClosed && h.isGroup && (d.dispatchEvent(new Event("mousedown")), t.preventDefault())
    }, Pe = new WeakSet, us = function(e, t) {
        var i;
        const s = Array.from(this.srcElement.querySelectorAll(".treeselect-list__item-checkbox")).filter(a => window.getComputedStyle(I(a)).display !== "none");
        if (s.length)
            if (!e) I(s[0]).classList.add("treeselect-list__item--focused");
            else {
                const a = s.findIndex(O => I(O).classList.contains("treeselect-list__item--focused"));
                I(s[a]).classList.remove("treeselect-list__item--focused");
                const d = t === "ArrowDown" ? a + 1 : a - 1,
                    w = t === "ArrowDown" ? 0 : s.length - 1,
                    g = s[d] ?? s[w],
                    k = !s[d],
                    E = I(g);
                E.classList.add("treeselect-list__item--focused");
                const v = this.srcElement.getBoundingClientRect(),
                    L = E.getBoundingClientRect();
                if (k && t === "ArrowDown") {
                    this.srcElement.scroll(0, 0);
                    return
                }
                if (k && t === "ArrowUp") {
                    this.srcElement.scroll(0, this.srcElement.scrollHeight);
                    return
                }
                const N = ((i = this.listSlotHtmlComponent) == null ? void 0 : i.clientHeight) ?? 0;
                if (v.y + v.height < L.y + L.height + N) {
                    this.srcElement.scroll(0, this.srcElement.scrollTop + L.height);
                    return
                }
                if (v.y > L.y) {
                    this.srcElement.scroll(0, this.srcElement.scrollTop - L.height);
                    return
                }
            }
    }, Be = new WeakSet, ps = function() {
        const e = o(this, Ve, ms).call(this),
            t = o(this, K, St).call(this, this.options);
        e.append(...t);
        const s = o(this, Me, gs).call(this);
        e.append(s);
        const i = o(this, Ge, Cs).call(this);
        return i && e.append(i), e
    }, Ve = new WeakSet, ms = function() {
        const e = document.createElement("div");
        return e.classList.add("treeselect-list"), this.isSingleSelect && e.classList.add("treeselect-list--single-select"), this.disabledBranchNode && e.classList.add("treeselect-list--disabled-branch-node"), e.addEventListener("mouseout", t => o(this, De, fs).call(this, t)), e.addEventListener("mousemove", () => o(this, He, bs).call(this)), e.addEventListener("mouseup", () => this.mouseupCallback(), !0), e
    }, De = new WeakSet, fs = function(e) {
        e.stopPropagation(), n(this, F) && n(this, V) && n(this, F).classList.add("treeselect-list__item--focused")
    }, He = new WeakSet, bs = function() {
        C(this, V, !0)
    }, K = new WeakSet, St = function(e) {
        return e.reduce((t, s) => {
            var a;
            if ((a = s.children) != null && a.length) {
                const h = o(this, Fe, ks).call(this, s),
                    d = o(this, K, St).call(this, s.children);
                return h.append(...d), t.push(h), t
            }
            const i = o(this, X, _t).call(this, s, !1);
            return t.push(i), t
        }, [])
    }, Ge = new WeakSet, Cs = function() {
        if (!this.listSlotHtmlComponent) return null;
        const e = document.createElement("div");
        return e.classList.add("treeselect-list__slot"), e.appendChild(this.listSlotHtmlComponent), e
    }, Me = new WeakSet, gs = function() {
        const e = document.createElement("div");
        e.classList.add("treeselect-list__empty"), e.setAttribute("title", this.emptyText);
        const t = document.createElement("span");
        t.classList.add("treeselect-list__empty-icon"), f(this.iconElements.attention, t);
        const s = document.createElement("span");
        return s.classList.add("treeselect-list__empty-text"), s.textContent = this.emptyText, e.append(t, s), e
    }, Fe = new WeakSet, ks = function(e) {
        const t = document.createElement("div");
        t.setAttribute("group-container-id", e.value.toString()), t.classList.add("treeselect-list__group-container");
        const s = o(this, X, _t).call(this, e, !0);
        return t.appendChild(s), t
    }, X = new WeakSet, _t = function(e, t) {
        const s = o(this, je, ws).call(this, e);
        if (t) {
            const h = o(this, We, ys).call(this);
            s.appendChild(h), s.classList.add("treeselect-list__item--group")
        }
        const i = o(this, ze, Ss).call(this, e),
            a = o(this, Ye, _s).call(this, e, t);
        return s.append(i, a), s
    }, je = new WeakSet, ws = function(e) {
        const t = document.createElement("div");
        return gi(t, e.htmlAttr), t.setAttribute("tabindex", "-1"), t.setAttribute("title", e.name), t.classList.add("treeselect-list__item"), t.addEventListener("mouseover", () => o(this, qe, Es).call(this, t), !0), t.addEventListener("mouseout", () => o(this, Re, vs).call(this, t), !0), t.addEventListener("mousedown", s => o(this, $e, Ls).call(this, s, e)), t
    }, qe = new WeakSet, Es = function(e) {
        n(this, V) && o(this, J, At).call(this, !0, e)
    }, Re = new WeakSet, vs = function(e) {
        n(this, V) && (o(this, J, At).call(this, !1, e), C(this, F, e))
    }, $e = new WeakSet, Ls = function(e, t) {
        var a;
        if (e.preventDefault(), e.stopPropagation(), (a = this.flattedOptions.find(h => h.id === t.value)) == null ? void 0 : a.disabled) return;
        const i = e.target.querySelector(".treeselect-list__item-checkbox");
        i.checked = !i.checked, o(this, Xe, Ts).call(this, i, t)
    }, We = new WeakSet, ys = function() {
        const e = document.createElement("span");
        return e.setAttribute("tabindex", "-1"), e.classList.add("treeselect-list__item-icon"), f(this.iconElements.arrowDown, e), e.addEventListener("mousedown", t => o(this, Ue, xs).call(this, t)), e
    }, Ue = new WeakSet, xs = function(e) {
        e.preventDefault(), e.stopPropagation(), o(this, Je, Ns).call(this, e)
    }, ze = new WeakSet, Ss = function(e) {
        const t = document.createElement("div");
        t.classList.add("treeselect-list__item-checkbox-container");
        const s = document.createElement("span");
        s.classList.add("treeselect-list__item-checkbox-icon"), s.innerHTML = "";
        const i = document.createElement("input");
        return i.setAttribute("tabindex", "-1"), i.setAttribute("type", "checkbox"), i.setAttribute("input-id", e.value.toString()), i.classList.add("treeselect-list__item-checkbox"), t.append(s, i), t
    }, Ye = new WeakSet, _s = function(e, t) {
        const s = document.createElement("label");
        if (s.textContent = e.name, s.classList.add("treeselect-list__item-label"), t && this.showCount) {
            const i = o(this, Ke, As).call(this, e);
            s.appendChild(i)
        }
        return s
    }, Ke = new WeakSet, As = function(e) {
        const t = document.createElement("span"),
            s = this.flattedOptions.filter(i => i.childOf === e.value);
        return t.textContent = `(${s.length})`, t.classList.add("treeselect-list__item-label-counter"), t
    }, Xe = new WeakSet, Ts = function(e, t) {
        const s = this.flattedOptions.find(i => i.id === t.value);
        if (s) {
            if (s != null && s.isGroup && this.disabledBranchNode) {
                const i = Ci(e);
                i == null || i.dispatchEvent(new Event("mousedown"));
                return
            }
            if (this.isSingleSelect) {
                const [i] = n(this, _);
                if (s.id === i) return;
                C(this, _, [s.id]), Dt([s.id], this.flattedOptions, this.isSingleSelect, this.isIndependentNodes)
            } else {
                s.checked = e.checked;
                const i = wt(s, this.flattedOptions, this.isIndependentNodes);
                e.checked = i
            }
            de(this.flattedOptions, this.srcElement, this.iconElements, n(this, _), this.rtl), o(this, Ze, Os).call(this)
        }
    }, Je = new WeakSet, Ns = function(e) {
        var a, h;
        const t = (h = (a = e.target) == null ? void 0 : a.parentNode) == null ? void 0 : h.querySelector("[input-id]"),
            s = (t == null ? void 0 : t.getAttribute("input-id")) ?? null,
            i = $t(s, this.flattedOptions);
        i && (i.isClosed = !i.isClosed, he(this.flattedOptions, i), de(this.flattedOptions, this.srcElement, this.iconElements, n(this, _), this.rtl), this.arrowClickCallback(i.id, i.isClosed))
    }, J = new WeakSet, At = function(e, t) {
        const s = "treeselect-list__item--focused";
        if (e) {
            const i = Array.from(this.srcElement.querySelectorAll(`.${s}`));
            i.length && i.forEach(a => a.classList.remove(s)), t.classList.add(s)
        } else t.classList.remove(s)
    }, Z = new WeakSet, Tt = function() {
        const {
            ungroupedNodes: e,
            groupedNodes: t,
            allNodes: s
        } = si(this.flattedOptions);
        this.selectedNodes = {
            nodes: e,
            groupedNodes: t,
            allNodes: s
        }
    }, Ze = new WeakSet, Os = function() {
        o(this, Z, Tt).call(this), this.inputCallback(this.selectedNodes), this.value = this.selectedNodes.nodes.map(e => e.id)
    };
    const Wt = ({
            parentHtmlContainer: l,
            staticList: e,
            appendToBody: t,
            isSingleSelect: s,
            value: i,
            direction: a
        }) => {
            l || console.error("Validation: parentHtmlContainer prop is required!"), e && t && console.error("Validation: You should set staticList to false if you use appendToBody!"), s && Array.isArray(i) && console.error("Validation: if you use isSingleSelect prop, you should pass a single value!"), !s && !Array.isArray(i) && console.error("Validation: you should pass an array as a value!"), a && a !== "auto" && a !== "bottom" && a !== "top" && console.error("Validation: you should pass (auto | top | bottom | undefined) as a value for the direction prop!")
        },
        ue = l => l.map(e => e.id),
        wi = l => l ? Array.isArray(l) ? l : [l] : [],
        Ei = (l, e) => {
            if (e) {
                const [t] = l;
                return t ?? null
            }
            return l
        };
    class vi {
        constructor({
            parentHtmlContainer: e,
            value: t,
            options: s,
            openLevel: i,
            appendToBody: a,
            alwaysOpen: h,
            showTags: d,
            tagsCountText: w,
            clearable: g,
            searchable: k,
            placeholder: E,
            grouped: v,
            isGroupedValue: L,
            listSlotHtmlComponent: N,
            disabled: O,
            emptyText: ae,
            staticList: bt,
            id: vt,
            ariaLabel: Lt,
            isSingleSelect: Ct,
            showCount: Li,
            disabledBranchNode: yi,
            direction: xi,
            expandSelected: Si,
            saveScrollPosition: _i,
            isIndependentNodes: Ai,
            rtl: Ti,
            iconElements: Ni,
            inputCallback: Oi,
            openCallback: Ii,
            closeCallback: Pi,
            nameChangeCallback: Bi,
            searchCallback: Vi,
            openCloseGroupCallback: Di
        }) {
            r(this, ee);
            r(this, te);
            r(this, Qe);
            r(this, et);
            r(this, tt);
            r(this, st);
            r(this, it);
            r(this, lt);
            r(this, se);
            r(this, nt);
            r(this, at);
            r(this, ot);
            r(this, rt);
            r(this, ie);
            r(this, ct);
            r(this, $);
            r(this, le);
            r(this, W);
            r(this, ht);
            r(this, ne);
            r(this, dt);
            r(this, ut);
            r(this, pt);
            r(this, mt);
            r(this, ft);
            c(this, "parentHtmlContainer");
            c(this, "value");
            c(this, "options");
            c(this, "openLevel");
            c(this, "appendToBody");
            c(this, "alwaysOpen");
            c(this, "showTags");
            c(this, "tagsCountText");
            c(this, "clearable");
            c(this, "searchable");
            c(this, "placeholder");
            c(this, "grouped");
            c(this, "isGroupedValue");
            c(this, "listSlotHtmlComponent");
            c(this, "disabled");
            c(this, "emptyText");
            c(this, "staticList");
            c(this, "id");
            c(this, "ariaLabel");
            c(this, "isSingleSelect");
            c(this, "showCount");
            c(this, "disabledBranchNode");
            c(this, "direction");
            c(this, "expandSelected");
            c(this, "saveScrollPosition");
            c(this, "isIndependentNodes");
            c(this, "rtl");
            c(this, "iconElements");
            c(this, "inputCallback");
            c(this, "openCallback");
            c(this, "closeCallback");
            c(this, "nameChangeCallback");
            c(this, "searchCallback");
            c(this, "openCloseGroupCallback");
            c(this, "ungroupedValue");
            c(this, "groupedValue");
            c(this, "allValue");
            c(this, "isListOpened");
            c(this, "selectedName");
            c(this, "selectedNameList");
            c(this, "selectedValue");
            c(this, "srcElement");
            r(this, u, null);
            r(this, m, null);
            r(this, q, null);
            r(this, Q, 0);
            r(this, R, 0);
            r(this, A, null);
            r(this, T, null);
            r(this, S, null);
            r(this, D, null);
            Wt({
                parentHtmlContainer: e,
                value: t,
                staticList: bt,
                appendToBody: a,
                isSingleSelect: Ct
            }), this.parentHtmlContainer = e, this.value = [], this.options = s ?? [], this.openLevel = i ?? 0, this.appendToBody = a ?? !1, this.alwaysOpen = !!(h && !O), this.showTags = d ?? !0, this.tagsCountText = w ?? "elements selected", this.clearable = g ?? !0, this.searchable = k ?? !0, this.placeholder = E ?? "Search...", this.grouped = v ?? !0, this.isGroupedValue = L ?? !1, this.listSlotHtmlComponent = N ?? null, this.disabled = O ?? !1, this.emptyText = ae ?? "No results found...", this.staticList = !!(bt && !this.appendToBody), this.id = vt ?? "", this.ariaLabel = Lt ?? "", this.isSingleSelect = Ct ?? !1, this.showCount = Li ?? !1, this.disabledBranchNode = yi ?? !1, this.direction = xi ?? "auto", this.expandSelected = Si ?? !1, this.saveScrollPosition = _i ?? !0, this.isIndependentNodes = Ai ?? !1, this.rtl = Ti ?? !1, this.iconElements = U(Ni), this.inputCallback = Oi, this.openCallback = Ii, this.closeCallback = Pi, this.nameChangeCallback = Bi, this.searchCallback = Vi, this.openCloseGroupCallback = Di, this.ungroupedValue = [], this.groupedValue = [], this.allValue = [], this.isListOpened = !1, this.selectedName = "", this.selectedNameList = [], this.srcElement = null, o(this, ee, Nt).call(this, t)
        }
        mount() {
            Wt({
                parentHtmlContainer: this.parentHtmlContainer,
                value: this.value,
                staticList: this.staticList,
                appendToBody: this.appendToBody,
                isSingleSelect: this.isSingleSelect
            }), this.iconElements = U(this.iconElements), o(this, ee, Nt).call(this, this.value)
        }
        updateValue(e) {
            const t = wi(e),
                s = n(this, u);
            s && (s.updateValue(t), o(this, se, It).call(this, s == null ? void 0 : s.selectedNodes))
        }
        destroy() {
            this.srcElement && (o(this, ie, Pt).call(this), this.srcElement.innerHTML = "", this.srcElement = null, o(this, W, kt).call(this, !0))
        }
        focus() {
            n(this, m) && n(this, m).focus()
        }
        toggleOpenClose() {
            n(this, m) && (n(this, m).openClose(), n(this, m).focus())
        }
        scrollWindowHandler() {
            this.updateListPosition()
        }
        focusWindowHandler(e) {
            var s, i, a;
            ((s = this.srcElement) == null ? void 0 : s.contains(e.target)) || ((i = n(this, u)) == null ? void 0 : i.srcElement.contains(e.target)) || ((a = n(this, m)) == null || a.blur(), o(this, W, kt).call(this, !1), o(this, $, gt).call(this, !1))
        }
        blurWindowHandler() {
            var e;
            (e = n(this, m)) == null || e.blur(), o(this, W, kt).call(this, !1), o(this, $, gt).call(this, !1)
        }
        updateListPosition() {
            var N;
            const e = this.srcElement,
                t = (N = n(this, u)) == null ? void 0 : N.srcElement;
            if (!e || !t) return;
            const {
                height: s
            } = t.getBoundingClientRect(), {
                x: i,
                y: a,
                height: h,
                width: d
            } = e.getBoundingClientRect(), w = window.innerHeight, g = a, k = w - a - h;
            let E = g > k && g >= s && k < s;
            if (this.direction !== "auto" && (E = this.direction === "top"), this.appendToBody) {
                (t.style.top !== "0px" || t.style.left !== "0px") && (t.style.top = "0px", t.style.left = "0px");
                const O = i + window.scrollX,
                    ae = E ? a + window.scrollY - s : a + window.scrollY + h;
                t.style.transform = `translate(${O}px,${ae}px)`, t.style.width = `${d}px`
            }
            const v = E ? "top" : "bottom";
            t.getAttribute("direction") !== v && (t.setAttribute("direction", v), o(this, ct, qs).call(this, E, this.appendToBody))
        }
    }
    return u = new WeakMap, m = new WeakMap, q = new WeakMap, Q = new WeakMap, R = new WeakMap, A = new WeakMap, T = new WeakMap, S = new WeakMap, D = new WeakMap, ee = new WeakSet, Nt = function(e) {
        var a;
        this.destroy();
        const {
            container: t,
            list: s,
            input: i
        } = o(this, Qe, Is).call(this);
        this.srcElement = t, C(this, u, s), C(this, m, i), C(this, A, this.scrollWindowHandler.bind(this)), C(this, T, this.scrollWindowHandler.bind(this)), C(this, S, this.focusWindowHandler.bind(this)), C(this, D, this.blurWindowHandler.bind(this)), this.alwaysOpen && ((a = n(this, m)) == null || a.openClose()), this.disabled ? this.srcElement.classList.add("treeselect--disabled") : this.srcElement.classList.remove("treeselect--disabled"), this.updateValue(e ?? this.value)
    }, te = new WeakSet, Ot = function({
        groupedNodes: e,
        nodes: t,
        allNodes: s
    }) {
        this.ungroupedValue = t ? ue(t) : [], this.groupedValue = e ? ue(e) : [], this.allValue = s ? ue(s) : [];
        let i = [];
        this.isIndependentNodes || this.isSingleSelect ? i = this.allValue : this.isGroupedValue ? i = this.groupedValue : i = this.ungroupedValue, this.value = Ei(i, this.isSingleSelect)
    }, Qe = new WeakSet, Is = function() {
        const e = this.parentHtmlContainer;
        e.classList.add("treeselect"), this.rtl && e.setAttribute("dir", "rtl");
        const t = new ki({
                value: [],
                options: this.options,
                openLevel: this.openLevel,
                listSlotHtmlComponent: this.listSlotHtmlComponent,
                emptyText: this.emptyText,
                isSingleSelect: this.isSingleSelect,
                showCount: this.showCount,
                disabledBranchNode: this.disabledBranchNode,
                expandSelected: this.expandSelected,
                isIndependentNodes: this.isIndependentNodes,
                rtl: this.rtl,
                iconElements: this.iconElements,
                inputCallback: i => o(this, nt, Gs).call(this, i),
                arrowClickCallback: (i, a) => o(this, at, Ms).call(this, i, a),
                mouseupCallback: () => {
                    var i;
                    return (i = n(this, m)) == null ? void 0 : i.focus()
                }
            }),
            s = new Xs({
                value: [],
                showTags: this.showTags,
                tagsCountText: this.tagsCountText,
                clearable: this.clearable,
                isAlwaysOpened: this.alwaysOpen,
                searchable: this.searchable,
                placeholder: this.placeholder,
                disabled: this.disabled,
                isSingleSelect: this.isSingleSelect,
                id: this.id,
                ariaLabel: this.ariaLabel,
                iconElements: this.iconElements,
                inputCallback: i => o(this, et, Ps).call(this, i),
                searchCallback: i => o(this, st, Vs).call(this, i),
                openCallback: () => o(this, rt, js).call(this),
                closeCallback: () => o(this, ie, Pt).call(this),
                keydownCallback: i => o(this, tt, Bs).call(this, i),
                focusCallback: () => o(this, it, Ds).call(this),
                blurCallback: () => o(this, lt, Hs).call(this),
                nameChangeCallback: (i, v) => o(this, ot, Fs).call(this, i, v)
            });
        return this.appendToBody && C(this, q, new ResizeObserver(() => this.updateListPosition())), e.append(s.srcElement), {
            container: e,
            list: t,
            input: s
        }
    }, et = new WeakSet, Ps = function(e) {
        var i, a;
        const t = ue(e);
        (i = n(this, u)) == null || i.updateValue(t);
        const s = ((a = n(this, u)) == null ? void 0 : a.selectedNodes) ?? {};
        o(this, te, Ot).call(this, s), o(this, ne, Vt).call(this)
    }, tt = new WeakSet, Bs = function(e) {
        var t;
        this.isListOpened && ((t = n(this, u)) == null || t.callKeyAction(e))
    }, st = new WeakSet, Vs = function(e) {
        n(this, R) && clearTimeout(n(this, R)), C(this, R, window.setTimeout(() => {
            var t;
            (t = n(this, u)) == null || t.updateSearchValue(e), this.updateListPosition()
        }, 350)), o(this, mt, zs).call(this, e)
    }, it = new WeakSet, Ds = function() {
        o(this, $, gt).call(this, !0), n(this, S) && n(this, S) && n(this, D) && (document.addEventListener("mousedown", n(this, S), !0), document.addEventListener("focus", n(this, S), !0), window.addEventListener("blur", n(this, D)))
    }, lt = new WeakSet, Hs = function() {
        setTimeout(() => {
            var s, i;
            const e = (s = n(this, m)) == null ? void 0 : s.srcElement.contains(document.activeElement),
                t = (i = n(this, u)) == null ? void 0 : i.srcElement.contains(document.activeElement);
            !e && !t && this.blurWindowHandler()
        }, 100)
    }, se = new WeakSet, It = function(e) {
        var s;
        if (!e) return;
        let t = [];
        this.isIndependentNodes || this.isSingleSelect ? t = e.allNodes : this.grouped ? t = e.groupedNodes : t = e.nodes, (s = n(this, m)) == null || s.updateValue(t), o(this, te, Ot).call(this, e)
    }, nt = new WeakSet, Gs = function(e) {
        var t, s, i;
        o(this, se, It).call(this, e), this.isSingleSelect && !this.alwaysOpen && ((t = n(this, m)) == null || t.openClose(), (s = n(this, m)) == null || s.clearSearch()), (i = n(this, m)) == null || i.focus(), o(this, ne, Vt).call(this)
    }, at = new WeakSet, Ms = function(e, t) {
        var s;
        (s = n(this, m)) == null || s.focus(), this.updateListPosition(), o(this, ft, Ys).call(this, e, t)
    }, ot = new WeakSet, Fs = function(e, v) {
        this.selectedName !== e && (this.selectedName = e, this.selectedNameList = e.split(', '), this.selectedValue = v && v.length > 0 ? v[0].id : null, o(this, dt, $s).call(this))
    }, rt = new WeakSet, js = function() {
        var e;
        this.isListOpened = !0, n(this, A) && n(this, T) && (window.addEventListener("scroll", n(this, A), !0), window.addEventListener("resize", n(this, T))), !(!n(this, u) || !this.srcElement) && (this.appendToBody ? (document.body.appendChild(n(this, u).srcElement), (e = n(this, q)) == null || e.observe(this.srcElement)) : this.srcElement.appendChild(n(this, u).srcElement), this.updateListPosition(), o(this, le, Bt).call(this, !0), o(this, ht, Rs).call(this), o(this, ut, Ws).call(this))
    }, ie = new WeakSet, Pt = function() {
        var t;
        this.alwaysOpen || (this.isListOpened = !1, n(this, A) && n(this, T) && (window.removeEventListener("scroll", n(this, A), !0), window.removeEventListener("resize", n(this, T))), !n(this, u) || !this.srcElement) || !(this.appendToBody ? document.body.contains(n(this, u).srcElement) : this.srcElement.contains(n(this, u).srcElement)) || (C(this, Q, n(this, u).srcElement.scrollTop), this.appendToBody ? (document.body.removeChild(n(this, u).srcElement), (t = n(this, q)) == null || t.disconnect()) : this.srcElement.removeChild(n(this, u).srcElement), o(this, le, Bt).call(this, !1), o(this, pt, Us).call(this))
    }, ct = new WeakSet, qs = function(e, t) {
        if (!n(this, u) || !n(this, m)) return;
        const s = t ? "treeselect-list--top-to-body" : "treeselect-list--top",
            i = t ? "treeselect-list--bottom-to-body" : "treeselect-list--bottom";
        e ? (n(this, u).srcElement.classList.add(s), n(this, u).srcElement.classList.remove(i), n(this, m).srcElement.classList.add("treeselect-input--top"), n(this, m).srcElement.classList.remove("treeselect-input--bottom")) : (n(this, u).srcElement.classList.remove(s), n(this, u).srcElement.classList.add(i), n(this, m).srcElement.classList.remove("treeselect-input--top"), n(this, m).srcElement.classList.add("treeselect-input--bottom"))
    }, $ = new WeakSet, gt = function(e) {
        !n(this, m) || !n(this, u) || (e ? (n(this, m).srcElement.classList.add("treeselect-input--focused"), n(this, u).srcElement.classList.add("treeselect-list--focused")) : (n(this, m).srcElement.classList.remove("treeselect-input--focused"), n(this, u).srcElement.classList.remove("treeselect-list--focused")))
    }, le = new WeakSet, Bt = function(e) {
        var t, s, i, a;
        e ? (t = n(this, m)) == null || t.srcElement.classList.add("treeselect-input--opened") : (s = n(this, m)) == null || s.srcElement.classList.remove("treeselect-input--opened"), this.staticList ? (i = n(this, u)) == null || i.srcElement.classList.add("treeselect-list--static") : (a = n(this, u)) == null || a.srcElement.classList.remove("treeselect-list--static")
    }, W = new WeakSet, kt = function(e) {
        !n(this, A) || !n(this, T) || !n(this, S) || !n(this, D) || ((!this.alwaysOpen || e) && (window.removeEventListener("scroll", n(this, A), !0), window.removeEventListener("resize", n(this, T))), document.removeEventListener("mousedown", n(this, S), !0), document.removeEventListener("focus", n(this, S), !0), window.removeEventListener("blur", n(this, D)))
    }, ht = new WeakSet, Rs = function() {
        var t, s, i;
        const e = (t = n(this, u)) == null ? void 0 : t.isLastFocusedElementExist();
        this.saveScrollPosition && e ? (s = n(this, u)) == null || s.srcElement.scroll(0, n(this, Q)) : (i = n(this, u)) == null || i.focusFirstListElement()
    }, ne = new WeakSet, Vt = function() {
        var e;
        (e = this.srcElement) == null || e.dispatchEvent(new CustomEvent("input", {
            detail: this.value
        })), this.inputCallback && this.inputCallback(this.value)
    }, dt = new WeakSet, $s = function() {
        var e;
        (e = this.srcElement) == null || e.dispatchEvent(new CustomEvent("name-change", {
            detail: this.selectedName
        })), this.nameChangeCallback && this.nameChangeCallback(this.selectedName)
    }, ut = new WeakSet, Ws = function() {
        var e;
        this.alwaysOpen || ((e = this.srcElement) == null || e.dispatchEvent(new CustomEvent("open", {
            detail: this.value
        })), this.openCallback && this.openCallback(this.value))
    }, pt = new WeakSet, Us = function() {
        var e;
        this.alwaysOpen || ((e = this.srcElement) == null || e.dispatchEvent(new CustomEvent("close", {
            detail: this.value
        })), this.closeCallback && this.closeCallback(this.value))
    }, mt = new WeakSet, zs = function(e) {
        var s;
        const t = (e == null ? void 0 : e.trim()) ?? "";
        (s = this.srcElement) == null || s.dispatchEvent(new CustomEvent("search", {
            detail: t
        })), this.searchCallback && this.searchCallback(t)
    }, ft = new WeakSet, Ys = function(e, t) {
        var s;
        (s = this.srcElement) == null || s.dispatchEvent(new CustomEvent("open-close-group", {
            detail: {
                groupId: e,
                isClosed: t
            }
        })), this.openCloseGroupCallback && this.openCloseGroupCallback(e, t)
    }, vi
});