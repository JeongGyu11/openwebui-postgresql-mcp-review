<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import Modal from '$lib/components/common/Modal.svelte';
	import { WEBUI_API_BASE_URL } from '$lib/constants';
	export let show = false;
	export let knowledgeId: string;
	const dispatch = createEventDispatcher();
	let host = '127.0.0.1';
	let port = 5432;
	let database = '';
	let username = '';
	let password = '';
	let rowLimit = 1000;
	let tables: { schema: string; name: string }[] = [];
	let selected = '';
	let preview: { columns: string[]; rows: Record<string, unknown>[]; has_more: boolean } | null = null;
	let busy = false;
	let error = '';
	let result = '';
	const inputClass = 'w-full rounded-lg border border-gray-200 dark:border-gray-700 bg-transparent px-3 py-2 text-sm';
	const reset = () => { tables = []; selected = ''; preview = null; result = ''; error = ''; };
	$: if (!show) password = '';
	const table = () => tables[Number(selected)];
	const connection = () => ({ host, port: Number(port), database, username, password });
	const request = async (action: string, body: object) => {
		const controller = new AbortController();
		const timer = setTimeout(() => controller.abort(), 30000);
		try {
			const response = await fetch(`${WEBUI_API_BASE_URL}/knowledge/${knowledgeId}/database/${action}`, {
				method: 'POST', headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${localStorage.token}` },
				body: JSON.stringify(body), signal: controller.signal
			});
			const data = await response.json();
			if (!response.ok) throw new Error(typeof data.detail === 'string' ? data.detail : '입력값을 확인하세요.');
			return data;
		} finally { clearTimeout(timer); }
	};
	const run = async (action: string) => {
		busy = true; error = ''; result = '';
		try {
			if (action === 'tables') {
				reset();
				const data = await request(action, connection());
				tables = data.tables;
				selected = tables.length ? '0' : '';
				if (!tables.length) result = '조회 가능한 테이블이 없습니다.';
			} else {
				const chosen = table();
				if (!chosen) throw new Error('테이블을 먼저 선택하세요.');
				const data = await request(action, { ...connection(), schema_name: chosen.schema, table_name: chosen.name, row_limit: Number(rowLimit) });
				if (action === 'preview') preview = data;
				else {
					result = `${chosen.schema}.${chosen.name}: ${data.row_count}건 ${data.updated ? '갱신' : '가져오기'} 완료. ${data.truncated ? '행 수 제한으로 일부 데이터만 가져왔습니다. ' : ''}원문은 저장되었으며 AI 의미검색 설정은 별도입니다.`;
					dispatch('imported', data);
				}
			}
		} catch (e) { error = e instanceof Error ? (e.name === 'AbortError' ? '요청 시간이 초과됐습니다. 연결 상태를 확인하세요.' : e.message) : '요청에 실패했습니다.'; }
		finally { busy = false; }
	};
	const close = () => { if (!busy) { password = ''; reset(); show = false; } };
</script>

<Modal bind:show size="lg" ariaLabel="PostgreSQL 테이블 연동" onRequestClose={close}>
	<div class="max-h-[85vh] overflow-y-auto p-5 text-gray-900 dark:text-gray-100">
		<div class="mb-2 flex items-center justify-between gap-3">
			<h2 class="text-lg font-semibold">PostgreSQL 테이블 연동</h2>
			<button type="button" aria-label="DB 연동 닫기" disabled={busy} on:click={close} class="rounded-lg px-3 py-1 disabled:opacity-50">닫기</button>
		</div>
		<p class="mb-4 text-sm text-gray-500">테이블을 선택해 현재 데이터를 지식기반 문서로 가져옵니다. 같은 테이블은 다시 가져오면 갱신되며 자동 동기화되지는 않습니다.</p>
		<form on:submit|preventDefault={() => run('tables')}>
			<fieldset disabled={busy} class="space-y-3 disabled:opacity-60">
				<div class="grid grid-cols-3 gap-3">
					<label class="col-span-2 text-sm">호스트<input class={inputClass} bind:value={host} on:input={reset} required autocomplete="off" /></label>
					<label class="text-sm">포트<input class={inputClass} type="number" min="1" max="65535" bind:value={port} on:input={reset} required /></label>
				</div>
				<label class="block text-sm">데이터베이스<input class={inputClass} bind:value={database} on:input={reset} required autocomplete="off" /></label>
				<div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
					<label class="text-sm">사용자<input class={inputClass} bind:value={username} on:input={reset} required autocomplete="off" /></label>
					<label class="text-sm">비밀번호<input class={inputClass} type="password" bind:value={password} on:input={reset} required autocomplete="off" /></label>
				</div>
				<p class="text-xs text-gray-500">조회 전용 계정을 권장합니다. 비밀번호는 연결 요청에만 사용하며 저장하지 않습니다.</p>
				<button type="submit" class="rounded-lg bg-gray-900 px-4 py-2 text-sm text-white dark:bg-gray-100 dark:text-gray-900">{busy ? '처리 중…' : '연결 및 테이블 조회'}</button>
			</fieldset>
		</form>
		{#if tables.length}
			<fieldset disabled={busy} class="mt-5 space-y-3 border-t border-gray-200 pt-4 dark:border-gray-700 disabled:opacity-60">
				<label class="block text-sm">테이블
					<select class={inputClass} bind:value={selected} on:change={() => { preview = null; result = ''; }}>
						{#each tables as item, index}<option value={String(index)}>{item.schema}.{item.name}</option>{/each}
					</select>
				</label>
				<label class="block text-sm">최대 행 수 (1~1000)<input class={inputClass} type="number" min="1" max="1000" bind:value={rowLimit} /></label>
				<div class="flex flex-wrap gap-2">
					<button type="button" class="rounded-lg border border-gray-300 px-4 py-2 text-sm" on:click={() => run('preview')}>미리보기 (최대 10건)</button>
					<button type="button" class="rounded-lg bg-blue-600 px-4 py-2 text-sm text-white" on:click={() => run('import')}>지식기반으로 가져오기</button>
				</div>
			</fieldset>
		{/if}
		{#if error}<p role="alert" class="mt-4 rounded-lg bg-red-50 p-3 text-sm text-red-700 dark:bg-red-950 dark:text-red-300">{error}</p>{/if}
		{#if result}<p role="status" class="mt-4 rounded-lg bg-green-50 p-3 text-sm text-green-800 dark:bg-green-950 dark:text-green-200">{result}</p>{/if}
		{#if preview}
			<p class="mt-4 text-sm">미리보기: {preview.rows.length}건{preview.has_more ? ' (추가 데이터 있음)' : ''}</p>
			<div class="mt-2 max-h-72 overflow-auto rounded-lg border border-gray-200 dark:border-gray-700">
				<table class="w-full text-left text-xs">
					<thead><tr>{#each preview.columns as column}<th class="whitespace-nowrap border-b p-2">{column}</th>{/each}</tr></thead>
					<tbody>{#each preview.rows as row}<tr>{#each preview.columns as column}<td class="min-w-24 max-w-xs border-b p-2 align-top break-words">{row[column] === null ? 'NULL' : typeof row[column] === 'object' ? JSON.stringify(row[column]) : String(row[column]).slice(0, 300)}</td>{/each}</tr>{/each}</tbody>
				</table>
			</div>
		{/if}
	</div>
</Modal>
