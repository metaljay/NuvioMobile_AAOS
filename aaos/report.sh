#!/usr/bin/env bash
# AAOS fork-owned. Lists every difference between this fork and the parent project, grouped by
# customisation (AAOS_FORK.md section 7), using the map in aaos/customisations.txt.
#
#   aaos/report.sh          summary: each customisation and the files it changes
#   aaos/report.sh C5       the summary, then the code changes for C5 (any ID works)
#   aaos/report.sh all      the summary, then the code changes for every customisation
#
# It compares the working folder with the last parent commit merged into it, so it also works
# in the middle of a parent update. Files that differ but are not in the map are listed under
# "NOT LABELLED": label them (or undo them) before a release.
set -euo pipefail
cd "$(git rev-parse --show-toplevel)"

map=aaos/customisations.txt
parent_ref=${AAOS_PARENT_REF:-upstream/cmp-rewrite}
show=${1:-}

if ! git rev-parse -q --verify "$parent_ref^{commit}" >/dev/null; then
    echo "Parent branch $parent_ref not found. Run: git fetch upstream" >&2
    exit 1
fi
base=$(git merge-base HEAD "$parent_ref")

# Map lines as "IDS<TAB>PATTERN".
map_lines=$(grep -v -E '^[[:space:]]*(#|$)' "$map" | awk '{print $1 "\t" $2}')

ids_for() {
    local file=$1 ids="" line pattern
    while IFS=$'\t' read -r line pattern; do
        # shellcheck disable=SC2053
        if [[ $file == $pattern ]]; then ids="$ids,$line"; fi
    done <<< "$map_lines"
    echo "${ids#,}"
}

# Every changed file (tracked or new), with lines added and removed.
changes=$(
    {
        git diff --no-renames --numstat "$base" -- .
        git ls-files --others --exclude-standard | while read -r f; do
            printf '%s\t0\t%s\n' "$(wc -l < "$f" | tr -d ' ')" "$f"
        done
    } | while IFS=$'\t' read -r added removed file; do
        if git cat-file -e "$base:$file" 2>/dev/null; then
            [[ -e $file ]] && kind="parent file" || kind="parent file DELETED"
        else
            kind="fork-only"
        fi
        printf '%s\t%s\t%s\t%s\t%s\n' "$(ids_for "$file")" "$added" "$removed" "$kind" "$file"
    done
)

title_for() {
    grep -E "^\| $1 \|" AAOS_FORK.md | head -1 | awk -F'|' '{gsub(/^ +| +$/, "", $3); print $3}'
}

echo "Fork vs parent commit $(git log -1 --format='%h (%ad)' --date=short "$base")"
echo "$(printf '%s\n' "$changes" | grep -c . || true) files differ from the parent."
echo

all_ids=$(printf '%s\n' "$changes" | cut -f1 | tr ',' '\n' | grep . | sort -u -t C -k2 -n || true)
for id in $all_ids; do
    echo "$id  $(title_for "$id")"
    printf '%s\n' "$changes" | awk -F'\t' -v id="$id" '
        { n = split($1, a, ","); for (i = 1; i <= n; i++) if (a[i] == id) printf "    +%-5s -%-5s %-20s %s\n", $2, $3, $4, $5 }'
    echo
done

unlabelled=$(printf '%s\n' "$changes" | awk -F'\t' '$1 == "" && $5 != "" { printf "    +%-5s -%-5s %-20s %s\n", $2, $3, $4, $5 }')
if [[ -n $unlabelled ]]; then
    echo "NOT LABELLED (add to $map, or undo):"
    echo "$unlabelled"
    echo
fi

if [[ -n $show ]]; then
    for id in $all_ids; do
        [[ $show == all || $show == "$id" ]] || continue
        files=$(printf '%s\n' "$changes" | awk -F'\t' -v id="$id" '
            { n = split($1, a, ","); for (i = 1; i <= n; i++) if (a[i] == id) print $5 }')
        echo "==================== $id  $(title_for "$id") ===================="
        for f in $files; do
            if git cat-file -e "$base:$f" 2>/dev/null; then
                git --no-pager diff "$base" -- "$f"
            else
                echo "---- fork-only file: $f"
                cat "$f"
            fi
        done
    done
fi
