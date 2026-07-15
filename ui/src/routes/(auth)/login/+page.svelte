<script lang="ts">
	import { resolve } from '$app/paths';
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard title="Login" backPath="/register" backTitle="Create new user">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			placeholder="Username or E-Mail*"
			required
			hint="Username is required"
			inputClass="w-full"
		/>
	</div>

	<div class="w-full">
		<Input
			type="password"
			name="password"
			autocomplete="current-password"
			required
			placeholder="Password*"
			hint="Password is required"
			inputClass="w-full"
		/>
	</div>

	<div class="flex w-full flex-col justify-between gap-2 sm:flex-row sm:items-center sm:gap-0">
		<div>
			<label class="label">
				<input type="checkbox" checked={false} class="checkbox checkbox-secondary" />
				Stay logged in
			</label>
		</div>
		<div>
			<a href={resolve('/forgot-password')} class="link link-primary"> Forgot Password? </a>
		</div>
	</div>

	<div class="mt-4 w-full">
		<Button type="submit" class="w-full btn-primary" label="Login" />
	</div>

	{#snippet afterForm()}
		<div class="divider">OR</div>

		<div class="indicator w-full">
			<Button class="w-full btn-sm" label="Login with Authentik" />
			<div class=" indicator-item badge badge-xs badge-info">Last used</div>
		</div>
	{/snippet}
</AuthCard>
