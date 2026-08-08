<script lang="ts">
	import { resolve } from '$app/paths';
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard backPath="/register" backTitle="Create new user" title="Login">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			hint="Username is required"
			inputClass="w-full"
			placeholder="Username or E-Mail*"
			required
		/>
	</div>

	<div class="w-full">
		<Input
			name="password"
			autocomplete="current-password"
			hint="Password is required"
			inputClass="w-full"
			placeholder="Password*"
			required
			type="password"
		/>
	</div>

	<div class="flex w-full flex-col justify-between gap-2 sm:flex-row sm:items-center sm:gap-0">
		<div>
			<label class="label">
				<input class="checkbox checkbox-secondary" checked={false} type="checkbox" />
				Stay logged in
			</label>
		</div>
		<div>
			<a class="link link-primary" href={resolve('/forgot-password')}> Forgot Password? </a>
		</div>
	</div>

	<div class="mt-4 w-full">
		<Button class="w-full btn-primary" label="Login" type="submit" />
	</div>

	{#snippet afterForm()}
		<div class="divider">OR</div>

		<div class="indicator w-full">
			<Button class="w-full btn-sm" label="Login with Authentik" />
			<div class=" indicator-item badge badge-xs badge-info">Last used</div>
		</div>
	{/snippet}
</AuthCard>
